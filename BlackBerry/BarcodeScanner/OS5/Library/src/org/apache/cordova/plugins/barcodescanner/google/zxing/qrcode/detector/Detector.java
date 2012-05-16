/*
 * Copyright 2007 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.cordova.plugins.barcodescanner.google.zxing.qrcode.detector;


import java.util.Hashtable;

import org.apache.cordova.plugins.barcodescanner.google.zxing.DecodeHintType;
import org.apache.cordova.plugins.barcodescanner.google.zxing.FormatException;
import org.apache.cordova.plugins.barcodescanner.google.zxing.NotFoundException;
import org.apache.cordova.plugins.barcodescanner.google.zxing.ResultPoint;
import org.apache.cordova.plugins.barcodescanner.google.zxing.ResultPointCallback;
import org.apache.cordova.plugins.barcodescanner.google.zxing.common.BitMatrix;
import org.apache.cordova.plugins.barcodescanner.google.zxing.common.DetectorResult;
import org.apache.cordova.plugins.barcodescanner.google.zxing.common.GridSampler;
import org.apache.cordova.plugins.barcodescanner.google.zxing.common.PerspectiveTransform;
import org.apache.cordova.plugins.barcodescanner.google.zxing.qrcode.decoder.Version;

/**
 * <p>Encapsulates logic that can detect a QR Code in an image, even if the QR Code
 * is rotated or skewed, or partially obscured.</p>
 *
 * @author Sean Owen
 */
public class Detector {

  private final BitMatrix image;
  private ResultPointCallback resultPointCallback;

  public Detector(BitMatrix image) {
    this.image = image;
  }

  protected BitMatrix getImage() {
    return image;
  }

  protected ResultPointCallback getResultPointCallback() {
    return resultPointCallback;
  }

  /**
   * <p>Detects a QR Code in an image, simply.</p>
   *
   * @return {@link DetectorResult} encapsulating results of detecting a QR Code
   * @throws NotFoundException if no QR Code can be found
   */
  public DetectorResult detect() throws NotFoundException, FormatException {
    return detect(null);
  }

  /**
   * <p>Detects a QR Code in an image, simply.</p>
   *
   * @param hints optional hints to detector
   * @return {@link NotFoundException} encapsulating results of detecting a QR Code
   * @throws NotFoundException if QR Code cannot be found
   * @throws FormatException if a QR Code cannot be decoded
   */
  public DetectorResult detect(Hashtable hints) throws NotFoundException, FormatException {

    resultPointCallback = hints == null ? null :
        (ResultPointCallback) hints.get(DecodeHintType.NEED_RESULT_POINT_CALLBACK);

    FinderPatternFinder finder = new FinderPatternFinder(image, resultPointCallback);
    FinderPatternInfo info = finder.find(hints);

    return processFinderPatternInfo(info);
  }

  protected DetectorResult processFinderPatternInfo(FinderPatternInfo info)
      throws NotFoundException, FormatException {

    FinderPattern topLeft = info.getTopLeft();
    FinderPattern topRight = info.getTopRight();
    FinderPattern bottomLeft = info.getBottomLeft();

    float moduleSize = calculateModuleSize(topLeft, topRight, bottomLeft);
    if (moduleSize < 1.0f) {
      throw NotFoundException.getNotFoundInstance();
    }
    int dimension = computeDimension(topLeft, topRight, bottomLeft, moduleSize);
    Version provisionalVersion = Version.getProvisionalVersionForDimension(dimension);
    int modulesBetweenFPCenters = provisionalVersion.getDimensionForVersion() - 7;

    AlignmentPattern alignmentPattern = null;
    // Anything above version 1 has an alignment pattern
    if (provisionalVersion.getAlignmentPatternCenters().length > 0) {

      // Guess where a "bottom right" finder pattern would have been
      float bottomRightX = topRight.getX() - topLeft.getX() + bottomLeft.getX();
      float bottomRightY = topRight.getY() - topLeft.getY() + bottomLeft.getY();

      // Estimate that alignment pattern is closer by 3 modules
      // from "bottom right" to known top left location
      float correctionToTopLeft = 1.0f - 3.0f / (float) modulesBetweenFPCenters;
      int estAlignmentX = (int) (topLeft.getX() + correctionToTopLeft * (bottomRightX - topLeft.getX()));
      int estAlignmentY = (int) (topLeft.getY() + correctionToTopLeft * (bottomRightY - topLeft.getY()));

      // Kind of arbitrary -- expand search radius before giving up
      for (int i = 4; i <= 16; i <<= 1) {
        try {
          alignmentPattern = findAlignmentInRegion(moduleSize,
              estAlignmentX,
              estAlignmentY,
              (float) i);
          break;
        } catch (NotFoundException re) {
          // try next round
        }
      }
      // If we didn't find alignment pattern... well try anyway without it
    }

    PerspectiveTransform transform =
        createTransform(topLeft, topRight, bottomLeft, alignmentPattern, dimension);

    BitMatrix bits = sampleGrid(image, transform, dimension);

    ResultPoint[] points;
    if (alignmentPattern == null) {
      points = new ResultPoint[]{bottomLeft, topLeft, topRight};
    } else {
      points = new ResultPoint[]{bottomLeft, topLeft, topRight, alignmentPattern};
    }
    return new DetectorResult(bits, points);
  }

  public static PerspectiveTransform createTransform(ResultPoint topLeft,
                                                     ResultPoint topRight,
                                                     ResultPoint bottomLeft,
                                                     ResultPoint alignmentPattern,
                                                     int dimension) {
    float dimMinusThree = (float) dimension - 3.5f;
    float bottomRightX;
    float bottomRightY;
    float sourceBottomRightX;
    float sourceBottomRightY;
    if (alignmentPattern != null) {
      bottomRightX = alignmentPattern.getX();
      bottomRightY = alignmentPattern.getY();
      sourceBottomRightX = sourceBottomRightY = dimMinusThree - 3.0f;
    } else {
      // Don't have an alignment pattern, just make up the bottom-right point
      bottomRightX = (topRight.getX() - topLeft.getX()) + bottomLeft.getX();
      bottomRightY = (topRight.getY() - topLeft.getY()) + bottomLeft.getY();
      sourceBottomRightX = sourceBottomRightY = dimMinusThree;
    }

    return PerspectiveTransform.quadrilateralToQuadrilateral(
        3.5f,
        3.5f,
        dimMinusThree,
        3.5f,
        sourceBottomRightX,
        sourceBottomRightY,
        3.5f,
        dimMinusThree,
        topLeft.getX(),
        topLeft.getY(),
        topRight.getX(),
        topRight.getY(),
        bottomRightX,
        bottomRightY,
        bottomLeft.getX(),
        bottomLeft.getY());
  }

  private static BitMatrix sampleGrid(BitMatrix image,
                                      PerspectiveTransform transform,
                                      int dimension) throws NotFoundException {

    GridSampler sampler = GridSampler.getInstance();
    return sampler.sampleGrid(image, dimension, dimension, transform);
  }

  /**
   * <p>Computes the dimension (number of modules on a size) of the QR Code based on the position
   * of the finder patterns and estimated module size.</p>
   */
  protected static int computeDimension(ResultPoint topLeft,
                                        ResultPoint topRight,
                                        ResultPoint bottomLeft,
                                      float moduleSize) throws NotFoundException {
    int tltrCentersDimension = round(ResultPoint.distance(topLeft, topRight) / moduleSize);
    int tlblCentersDimension = round(ResultPoint.distance(topLeft, bottomLeft) / moduleSize);
    int dimension = ((tltrCentersDimension + tlblCentersDimension) >> 1) + 7;
    switch (dimension & 0x03) { // mod 4
      case 0:
        dimension++;
        break;
        // 1? do nothing
      case 2:
        dimension--;
        break;
      case 3:
        throw NotFoundException.getNotFoundInstance();
    }
    return dimension;
  }

  /**
   * <p>Computes an average estimated module size based on estimated derived from the positions
   * of the three finder patterns.</p>
   */
  protected float calculateModuleSize(ResultPoint topLeft,
                                      ResultPoint topRight,
                                      ResultPoint bottomLeft) {
    // Take the average
    return (calculateModuleSizeOneWay(topLeft, topRight) +
        calculateModuleSizeOneWay(topLeft, bottomLeft)) / 2.0f;
  }

  /**
   * <p>Estimates module size based on two finder patterns -- it uses
   * {@link #sizeOfBlackWhiteBlackRunBothWays(int, int, int, int)} to figure the
   * width of each, measuring along the axis between their centers.</p>
   */
  private float calculateModuleSizeOneWay(ResultPoint pattern, ResultPoint otherPattern) {
    float moduleSizeEst1 = sizeOfBlackWhiteBlackRunBothWays((int) pattern.getX(),
        (int) pattern.getY(),
        (int) otherPattern.getX(),
        (int) otherPattern.getY());
    float moduleSizeEst2 = sizeOfBlackWhiteBlackRunBothWays((int) otherPattern.getX(),
        (int) otherPattern.getY(),
        (int) pattern.getX(),
        (int) pattern.getY());
    if (Float.isNaN(moduleSizeEst1)) {
      return moduleSizeEst2 / 7.0f;
    }
    if (Float.isNaN(moduleSizeEst2)) {
      return moduleSizeEst1 / 7.0f;
    }
    // Average them, and divide by 7 since we've counted the width of 3 black modules,
    // and 1 white and 1 black module on either side. Ergo, divide sum by 14.
    return (moduleSizeEst1 + moduleSizeEst2) / 14.0f;
  }

  /**
   * See {@link #sizeOfBlackWhiteBlackRun(int, int, int, int)}; computes the total width of
   * a finder pattern by looking for a black-white-black run from the center in the direction
   * of another point (another finder pattern center), and in the opposite direction too.</p>
   */
  private float sizeOfBlackWhiteBlackRunBothWays(int fromX, int fromY, int toX, int toY) {

   float result = sizeOfBlackWhiteBlackRun(fromX, fromY, toX, toY);

   // Now count other way -- don't run off image though of course
   float scale = 1.0f;
   int otherToX = fromX - (toX - fromX);
   if (otherToX < 0) {
     scale = (float) fromX / (float) (fromX - otherToX);
     otherToX = 0;
   } else if (otherToX > image.getWidth()) {
     scale = (float) (image.getWidth() - fromX) / (float) (otherToX - fromX);
     otherToX = image.getWidth();
   }
   int otherToY = (int) (fromY - (toY - fromY) * scale);

   scale = 1.0f;
   if (otherToY < 0) {
     scale = (float) fromY / (float) (fromY - otherToY);
     otherToY = 0;
   } else if (otherToY > image.getHeight()) {
     scale = (float) (image.getHeight() - fromY) / (float) (otherToY - fromY);
     otherToY = image.getHeight();
   }
   otherToX = (int) (fromX + (otherToX - fromX) * scale);

   result += sizeOfBlackWhiteBlackRun(fromX, fromY, otherToX, otherToY);
   return result;
 }

  /**
   * <p>This method traces a line from a point in the image, in the direction towards another point.
   * It begins in a black region, and keeps going until it finds white, then black, then white again.
   * It reports the distance from the start to this point.</p>
   *
   * <p>This is used when figuring out how wide a finder pattern is, when the finder pattern
   * may be skewed or rotated.</p>
   */
  private float sizeOfBlackWhiteBlackRun(int fromX, int fromY, int toX, int toY) {
    // Mild variant of Bresenham's algorithm;
    // see http://en.wikipedia.org/wiki/Bresenham's_line_algorithm
    boolean steep = Math.abs(toY - fromY) > Math.abs(toX - fromX);
    if (steep) {
      int temp = fromX;
      fromX = fromY;
      fromY = temp;
      temp = toX;
      toX = toY;
      toY = temp;
    }

    int dx = Math.abs(toX - fromX);
    int dy = Math.abs(toY - fromY);
    int error = -dx >> 1;
    int xstep = fromX < toX ? 1 : -1;
    int ystep = fromY < toY ? 1 : -1;

    // In black pixels, looking for white, first or second time.
    int state = 0;
    for (int x = fromX, y = fromY; x != toX; x += xstep) {
      int realX = steep ? y : x;
      int realY = steep ? x : y;

      // In white pixels, looking for black.
      // FIXME(dswitkin): This method seems to assume square images, which can cause these calls to
      // BitMatrix.get() to throw ArrayIndexOutOfBoundsException.
      if (state == 1) {
        if (image.get(realX, realY)) {
          state++;
        }
      } else {
        if (!image.get(realX, realY)) {
          state++;
        }
      }

      // Found black, white, black, and stumbled back onto white, so we're done.
      if (state == 3) {
        int diffX = x - fromX;
        int diffY = y - fromY;
        if (xstep < 0) {
            diffX++;
        }
        return (float) Math.sqrt((double) (diffX * diffX + diffY * diffY));
      }
      error += dy;
      if (error > 0) {
        if (y == toY) {
          break;
        }
        y += ystep;
        error -= dx;
      }
    }
    int diffX = toX - fromX;
    int diffY = toY - fromY;
    return (float) Math.sqrt((double) (diffX * diffX + diffY * diffY));
  }

  /**
   * <p>Attempts to locate an alignment pattern in a limited region of the image, which is
   * guessed to contain it. This method uses {@link AlignmentPattern}.</p>
   *
   * @param overallEstModuleSize estimated module size so far
   * @param estAlignmentX x coordinate of center of area probably containing alignment pattern
   * @param estAlignmentY y coordinate of above
   * @param allowanceFactor number of pixels in all directions to search from the center
   * @return {@link AlignmentPattern} if found, or null otherwise
   * @throws NotFoundException if an unexpected error occurs during detection
   */
  protected AlignmentPattern findAlignmentInRegion(float overallEstModuleSize,
                                                   int estAlignmentX,
                                                   int estAlignmentY,
                                                   float allowanceFactor)
      throws NotFoundException {
    // Look for an alignment pattern (3 modules in size) around where it
    // should be
    int allowance = (int) (allowanceFactor * overallEstModuleSize);
    int alignmentAreaLeftX = Math.max(0, estAlignmentX - allowance);
    int alignmentAreaRightX = Math.min(image.getWidth() - 1, estAlignmentX + allowance);
    if (alignmentAreaRightX - alignmentAreaLeftX < overallEstModuleSize * 3) {
      throw NotFoundException.getNotFoundInstance();
    }

    int alignmentAreaTopY = Math.max(0, estAlignmentY - allowance);
    int alignmentAreaBottomY = Math.min(image.getHeight() - 1, estAlignmentY + allowance);
    if (alignmentAreaBottomY - alignmentAreaTopY < overallEstModuleSize * 3) {
      throw NotFoundException.getNotFoundInstance();
    }

    AlignmentPatternFinder alignmentFinder =
        new AlignmentPatternFinder(
            image,
            alignmentAreaLeftX,
            alignmentAreaTopY,
            alignmentAreaRightX - alignmentAreaLeftX,
            alignmentAreaBottomY - alignmentAreaTopY,
            overallEstModuleSize,
            resultPointCallback);
    return alignmentFinder.find();
  }

  /**
   * Ends up being a bit faster than Math.round(). This merely rounds its argument to the nearest int,
   * where x.5 rounds up.
   */
  private static int round(float d) {
    return (int) (d + 0.5f);
  }
}
