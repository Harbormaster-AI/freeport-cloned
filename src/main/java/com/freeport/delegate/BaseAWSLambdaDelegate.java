/*******************************************************************************
  Turnstone Biologics Confidential
  
  2018 Turnstone Biologics
  All Rights Reserved.
  
  This file is subject to the terms and conditions defined in
  file 'license.txt', which is part of this source code package.
   
  Contributors :
        Turnstone Biologics - General Release
 ******************************************************************************/
package com.freeport.delegate;

import com.amazonaws.services.lambda.runtime.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.*;


/**
 * Base class for AWS Lambda business delegates.
 * <p>
 * @author Dev Team
 */
public class BaseAWSLambdaDelegate
{
    protected BaseAWSLambdaDelegate()
    {
    }

    protected static String getContextDetails( Context context ) {
    	StringBuilder details = new StringBuilder();
    	
    	if ( context != null )
    	{
	    	details.append("Function name: " + context.getFunctionName());
	        details.append("\nMax mem allocated: " + context.getMemoryLimitInMB());
	        details.append("\nTime remaining in milliseconds: " + context.getRemainingTimeInMillis());
	        details.append("\nCloudWatch log stream name: " + context.getLogStreamName());
	        details.append("\nCloudWatch log group name: " + context.getLogGroupName()); 
	        details.append("\n");
    	}
    	return( details.toString() );
    }
    
    
// attributes
    private static Context context 					= null;
  
}

