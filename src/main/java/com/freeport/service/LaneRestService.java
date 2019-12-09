/*******************************************************************************
  Turnstone Biologics Confidential
  
  2018 Turnstone Biologics
  All Rights Reserved.
  
  This file is subject to the terms and conditions defined in
  file 'license.txt', which is part of this source code package.
   
  Contributors :
        Turnstone Biologics - General Release
 ******************************************************************************/
package com.freeport.service;

import java.text.SimpleDateFormat;
import java.util.logging.Logger;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import static spark.Spark.get;
import static spark.Spark.post;

import spark.Request;
import spark.Response;
import spark.Route;

import com.freeport.bo.Base;
import com.freeport.bo.*;
import com.freeport.common.JsonTransformer;
import com.freeport.delegate.*;
import com.freeport.primarykey.*;
import com.freeport.exception.ProcessingException;

/** 
 * Implements Struts action processing for business entity Lane.
 *
 * @author Dev Team
 */

public class LaneRestService extends BaseRestService
{

	public LaneRestService()
	{}
	
    /**
     * Handles saving a Lane BO.  if not key provided, calls create, otherwise calls save
     * @exception	ProcessingException
     */
    protected Lane save()
    	throws ProcessingException
    {
		// doing it here helps
		getLane();

		LOGGER.info( "Lane.save() on - " + lane );
		
        if ( hasPrimaryKey() )
        {
            return (update());
        }
        else
        {
            return (create());
        }
    }

    /**
     * Returns true if the lane is non-null and has it's primary key field(s) set
     * @return		boolean
     */
    protected boolean hasPrimaryKey()
    {
    	boolean hasPK = false;

		if ( lane != null && lane.getLanePrimaryKey().hasBeenAssigned() == true )
		   hasPK = true;
		
		return( hasPK );
    }

    /**
     * Handles updating a Lane BO
     * @return		Lane
     * @exception	ProcessingException
     */    
    protected Lane update()
    	throws ProcessingException
    {
    	// store provided data
        Lane tmp = lane;

        // load actual data from storage
        loadHelper( lane.getLanePrimaryKey() );
    	
    	// copy provided data into actual data
    	lane.copyShallow( tmp );
    	
        try
        {                        	        
			// create the LaneBusiness Delegate            
			LaneBusinessDelegate delegate = LaneBusinessDelegate.getLaneInstance();
            this.lane = delegate.saveLane( lane );
            
            if ( this.lane != null )
            	LOGGER.info( "LaneRestService:update() - successfully updated Lane - " + lane.toString() );
        }
        catch( Throwable exc )
        {
        	signalBadRequest();
        	
        	String errMsg = "LaneRestService:update() - successfully update Lane - " + exc.getMessage();
        	LOGGER.severe( errMsg );
        	throw new ProcessingException( errMsg );
        }
        
        return this.lane;
        
    }

    /**
     * Handles creating a Lane BO
     * @return		Lane
     */
    protected Lane create()
    	throws ProcessingException
    {
        try
        {       
        	lane 		= getLane();
			this.lane 	= LaneBusinessDelegate.getLaneInstance().createLane( lane );
        }
        catch( Throwable exc )
        {
        	signalBadRequest();
        	
        	String errMsg = "LaneRestService:create() - exception Lane - " + exc.getMessage();
        	LOGGER.severe( errMsg );
        	throw new ProcessingException( errMsg );
        }
        
        return this.lane;
    }


    
    /**
     * Handles deleting a Lane BO
     * @exception	ProcessingException
     */
    protected void delete() 
    	throws ProcessingException
    {                
        try
        {
        	LaneBusinessDelegate delegate = LaneBusinessDelegate.getLaneInstance();

        	Long[] childIds = getChildIds();
        	
        	if ( childIds == null || childIds.length == 0 )
        	{
        		Long laneId  = parseId( "laneId" );
        		delegate.delete( new LanePrimaryKey( laneId  ) );
        		LOGGER.info( "LaneRestService:delete() - successfully deleted Lane with key " + lane.getLanePrimaryKey().valuesAsCollection() );
        	}
        	else
        	{
        		for ( Long id : childIds )
        		{
        			try
        			{
        				delegate.delete( new LanePrimaryKey( id ) );
        			}
	                catch( Throwable exc )
	                {
	                	signalBadRequest();

	                	String errMsg = "LaneRestService:delete() - " + exc.getMessage();
	                	LOGGER.severe( errMsg );
	                	throw new ProcessingException( errMsg );
	                }
        		}
        	}
        }
        catch( Throwable exc )
        {
        	signalBadRequest();
        	String errMsg = "LaneRestService:delete() - " + exc.getMessage();
        	LOGGER.severe( errMsg );
        	throw new ProcessingException( errMsg );
        }
	}        
	
    /**
     * Handles loading a Lane BO
     * @param		Long laneId
     * @exception	ProcessingException
     * @return		Lane
     */    
    protected Lane load() 
    	throws ProcessingException
    {    	
        LanePrimaryKey pk 	= null;
		Long laneId  = parseId( "laneId" );

    	try
        {
    		LOGGER.info( "Lane.load pk is " + laneId );
    		
        	if ( laneId != null )
        	{
        		pk = new LanePrimaryKey( laneId );

        		loadHelper( pk );

        		// load the contained instance of Lane
	            this.lane = LaneBusinessDelegate.getLaneInstance().getLane( pk );
	            
	            LOGGER.info( "LaneRestService:load() - successfully loaded - " + this.lane.toString() );             
			}
			else
			{
	        	signalBadRequest();

				String errMsg = "LaneRestService:load() - unable to locate the primary key as an attribute or a selection for - " + lane.toString();				
				LOGGER.severe( errMsg );
	            throw new ProcessingException( errMsg );
			}	            
        }
        catch( Throwable exc )
        {
        	signalBadRequest();

        	String errMsg = "LaneRestService:load() - failed to load Lane using Id " + laneId + ", " + exc.getMessage();				
			LOGGER.severe( errMsg );
            throw new ProcessingException( errMsg );
        }

        return lane;

    }

    /**
     * Handles loading all Lane business objects
     * @return		List<Lane>
     * @exception	ProcessingException
     */
    protected List<Lane> loadAll()
    	throws ProcessingException
    {                
        List<Lane> laneList = null;
        
    	try
        {                        
            // load the Lane
            laneList = LaneBusinessDelegate.getLaneInstance().getAllLane();
            
            if ( laneList != null )
            	LOGGER.info(  "LaneRestService:loadAllLane() - successfully loaded all Lanes" );
        }
        catch( Throwable exc )
        {
        	signalBadRequest();

        	String errMsg = "LaneRestService:loadAll() - failed to load all Lanes - " + exc.getMessage();				
			LOGGER.severe( errMsg );
            throw new ProcessingException( errMsg );            
        }

        return laneList;
                            
    }






    protected Lane loadHelper( LanePrimaryKey pk )
    		throws ProcessingException
    {
    	try
        {
    		LOGGER.info( "Lane.loadHelper primary key is " + pk);
    		
        	if ( pk != null )
        	{
        		// load the contained instance of Lane
	            this.lane = LaneBusinessDelegate.getLaneInstance().getLane( pk );
	            
	            LOGGER.info( "LaneRestService:loadHelper() - successfully loaded - " + this.lane.toString() );             
			}
			else
			{
	        	signalBadRequest();

				String errMsg = "LaneRestService:loadHelper() - null primary key provided.";				
				LOGGER.severe( errMsg );
	            throw new ProcessingException( errMsg );
			}	            
        }
        catch( Throwable exc )
        {
        	signalBadRequest();

        	String errMsg = "LaneRestService:load() - failed to load Lane using pk " + pk + ", " + exc.getMessage();				
			LOGGER.severe( errMsg );
            throw new ProcessingException( errMsg );
        }

        return lane;

    }

    // overloads from BaseRestService
    
    /**
     * main handler for execution
     * @param action
     * @param response
     * @param request
     * @return
     * @throws ProcessingException
     */
	public Object handleExec( String action, spark.Response response, spark.Request request )
		throws ProcessingException
	{
		// store locally
		this.response = response;
		this.request = request;
				
		if ( action == null )
		{
			signalBadRequest();
			throw new ProcessingException();
		}

		Object returnVal = null;

		switch (action) {
	        case "save":
	        	returnVal = save();
	            break;
	        case "load":
	        	returnVal = load();
	            break;
	        case "delete":
	        	delete();
	            break;
	        case "loadAll":
	        case "viewAll":
	        	returnVal = loadAll();
	            break;
          default:
        	signalBadRequest();
            throw new ProcessingException("Lane.execute(...) - unable to handle action " + action);
		}
		
		return returnVal;
	}
	
	/**
	 * Uses ObjectMapper to map from Json to a Lane. Found in the request body.
	 * 
	 * @return Lane
	 */
	private Lane getLane()
	{
		if ( lane == null )
		{
			try {
				ObjectMapper mapper = new ObjectMapper();
				mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
				lane = mapper.readValue(java.net.URLDecoder.decode(request.queryString(),"UTF-8"), Lane.class);
				
	        } catch (Exception exc) 
	        {
	            signalBadRequest();
	            LOGGER.severe( "LaneRestService.getLane() - failed to Json map from String to Lane - " + exc.getMessage() );
	        }			
		}
		return( lane );
	}
	
	protected String getSubclassName()
	{ return( "LaneRestService" ); }
	
//************************************************************************    
// Attributes
//************************************************************************
    private Lane lane 			= null;
    private static final Logger LOGGER 	= Logger.getLogger(BaseRestService.class.getName());
    
}
