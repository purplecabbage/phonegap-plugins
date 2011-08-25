//
//  InAppPurchaseManager.m
//  beetight
//
//  Created by Matt Kane on 20/02/2011.
//  Copyright 2011 Matt Kane. All rights reserved.
//

#import "InAppPurchaseManager.h"


@implementation InAppPurchaseManager

-(void) setup:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options {
    [[SKPaymentQueue defaultQueue] addTransactionObserver:self];
}

- (void) requestProductData:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options 
{
	if([arguments count] < 3) {
		return;	
	}
	NSLog(@"Getting product data");
	//NSSet *productIdentifiers = [NSSet setWithObject:[arguments objectAtIndex:0]];
	NSSet *productIdentifiers = [NSSet setWithArray:[(NSString*)[options objectForKey:@"productIds"] componentsSeparatedByString:@" "]];
    SKProductsRequest *productsRequest = [[SKProductsRequest alloc] initWithProductIdentifiers:productIdentifiers];
	
	ProductsRequestDelegate* delegate = [[[ProductsRequestDelegate alloc] init] retain];
	delegate.command = self;
	delegate.successCallback = [arguments objectAtIndex:1];
	delegate.failCallback = [arguments objectAtIndex:2];
	
    productsRequest.delegate = delegate;
    [productsRequest start];
    
}

- (void) makePurchase:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options 
{
	NSLog(@"About to do IAP");
	if([arguments count] < 1) {
		return;	
	}
	
    SKMutablePayment *payment = [SKMutablePayment paymentWithProductIdentifier:[arguments objectAtIndex:0]];
	
	if([arguments count] > 1) {
		id quantity = [arguments objectAtIndex:1];
		if ([quantity respondsToSelector:@selector(integerValue)]) {
			payment.quantity = [quantity integerValue];	
		}
	}
	[[SKPaymentQueue defaultQueue] addPayment:payment];
}

- (void) restoreCompletedTransactions:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options 
{
    [[SKPaymentQueue defaultQueue] restoreCompletedTransactions];
}

// SKPaymentTransactionObserver methods
// called when the transaction status is updated 
//
- (void)paymentQueue:(SKPaymentQueue *)queue updatedTransactions:(NSArray *)transactions
{
	NSString *state, *error, *transactionIdentifier, *transactionReceipt, *productId;
	NSInteger errorCode;
	
    for (SKPaymentTransaction *transaction in transactions)
    {
		error = state = transactionIdentifier = transactionReceipt = productId = @"";
		errorCode = 0;
		
        switch (transaction.transactionState)
        {
            case SKPaymentTransactionStatePurchased:
				state = @"PaymentTransactionStatePurchased";
				transactionIdentifier = transaction.transactionIdentifier;
				transactionReceipt = [[transaction transactionReceipt] base64EncodedString];
				productId = transaction.payment.productIdentifier;
                break;
            
			case SKPaymentTransactionStateFailed:
				state = @"PaymentTransactionStateFailed";
				error = transaction.error.localizedDescription;
				errorCode = transaction.error.code;
				NSLog(@"error %d %@", errorCode, error);

                break;
            
			case SKPaymentTransactionStateRestored:
				state = @"PaymentTransactionStateRestored";
				transactionIdentifier = transaction.originalTransaction.transactionIdentifier;
				transactionReceipt = [[[transaction originalTransaction] transactionReceipt] base64EncodedString];
				productId = transaction.originalTransaction.payment.productIdentifier;
                break;
				
            default:
				NSLog(@"Invalid state");
                continue;
        }
		NSLog(@"state: %@", state);
		NSString *js = [NSString stringWithFormat:@"InAppPurchaseManager.manager.updatedTransactionCallback('%@',%d, '%@','%@','%@','%@')", state, errorCode, error, transactionIdentifier, productId, transactionReceipt ];
		NSLog(@"js: %@", js);
		[self writeJavascript: js];
		[[SKPaymentQueue defaultQueue] finishTransaction:transaction];

    }
}


@end

@implementation ProductsRequestDelegate 

@synthesize successCallback, failCallback, command;


- (void)productsRequest:(SKProductsRequest *)request didReceiveResponse:(SKProductsResponse *)response
{
	NSLog(@"got iap product response");
    for (SKProduct *product in response.products) {
		NSLog(@"sending js for %@", product.productIdentifier);
		NSString *js = [NSString stringWithFormat:@"%@('%@','%@','%@','%@')", successCallback, product.productIdentifier, product.localizedTitle, product.localizedDescription, product.localizedPrice];
		NSLog(@"js: %@", js);
		[command writeJavascript: js];
    }

    for (NSString *invalidProductId in response.invalidProductIdentifiers) {
		NSLog(@"sending fail (%@) js for %@", failCallback, invalidProductId);

		[command writeJavascript: [NSString stringWithFormat:@"%@('%@')", failCallback, invalidProductId]];
    }
	NSLog(@"done iap");

	[command writeJavascript: [NSString stringWithFormat:@"%@('__DONE')", successCallback]];

	[request release];
	[self release];
}

- (void) dealloc
{
    [successCallback release];
    [failCallback release];
	[command release];
    [super dealloc];
}


@end
