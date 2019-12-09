/*******************************************************************************
  Turnstone Biologics Confidential
  
  2018 Turnstone Biologics
  All Rights Reserved.
  
  This file is subject to the terms and conditions defined in
  file 'license.txt', which is part of this source code package.
   
  Contributors :
        Turnstone Biologics - General Release
 ******************************************************************************/
package com.freeport.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

    import com.freeport.primarykey.*;
    import com.freeport.delegate.*;
    import com.freeport.bo.*;
    
/** 
 * Implements Struts action processing for business entity Lane.
 *
 * @author Dev Team
 */
@RestController
@RequestMapping("/Lane")
public class LaneRestController extends BaseSpringRestController
{

    /**
     * Handles saving a Lane BO.  if not key provided, calls create, otherwise calls save
     * @param		Lane lane
     * @return		Lane
     */
	@RequestMapping("/save")
    public Lane save( @RequestBody Lane lane )
    {
    	// assign locally
    	this.lane = lane;
    	
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
     * Handles deleting a Lane BO
     * @param		Long laneId
     * @param 		Long[] childIds
     * @return		boolean
     */
    @RequestMapping("/delete")    
    public boolean delete( @RequestParam(value="lane.laneId", required=false) Long laneId, 
    						@RequestParam(value="childIds", required=false) Long[] childIds )
    {                
        try
        {
        	LaneBusinessDelegate delegate = LaneBusinessDelegate.getLaneInstance();
        	
        	if ( childIds == null || childIds.length == 0 )
        	{
        		delegate.delete( new LanePrimaryKey( laneId ) );
        		LOGGER.info( "LaneController:delete() - successfully deleted Lane with key " + lane.getLanePrimaryKey().valuesAsCollection() );
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
	                	LOGGER.info( "LaneController:delete() - " + exc.getMessage() );
	                	return false;
	                }
        		}
        	}
        }
        catch( Throwable exc )
        {
        	LOGGER.info( "LaneController:delete() - " + exc.getMessage() );
        	return false;        	
        }
        
        return true;
	}        
	
    /**
     * Handles loading a Lane BO
     * @param		Long laneId
     * @return		Lane
     */    
    @RequestMapping("/load")
    public Lane load( @RequestParam(value="lane.laneId", required=true) Long laneId )
    {    	
        LanePrimaryKey pk = null;

    	try
        {  
    		System.out.println( "\n\n****Lane.load pk is " + laneId );
        	if ( laneId != null )
        	{
        		pk = new LanePrimaryKey( laneId );
        		
        		// load the Lane
	            this.lane = LaneBusinessDelegate.getLaneInstance().getLane( pk );
	            
	            LOGGER.info( "LaneController:load() - successfully loaded - " + this.lane.toString() );             
			}
			else
			{
	            LOGGER.info( "LaneController:load() - unable to locate the primary key as an attribute or a selection for - " + lane.toString() );
	            return null;
			}	            
        }
        catch( Throwable exc )
        {
            LOGGER.info( "LaneController:load() - failed to load Lane using Id " + laneId + ", " + exc.getMessage() );
            return null;
        }

        return lane;

    }

    /**
     * Handles loading all Lane business objects
     * @return		List<Lane>
     */
    @RequestMapping("/loadAll")
    public List<Lane> loadAll()
    {                
        List<Lane> laneList = null;
        
    	try
        {                        
            // load the Lane
            laneList = LaneBusinessDelegate.getLaneInstance().getAllLane();
            
            if ( laneList != null )
                LOGGER.info(  "LaneController:loadAllLane() - successfully loaded all Lanes" );
        }
        catch( Throwable exc )
        {
            LOGGER.info(  "LaneController:loadAll() - failed to load all Lanes - " + exc.getMessage() );
        	return null;
            
        }

        return laneList;
                            
    }


// findAllBy methods




    /**
     * Handles creating a Lane BO
     * @return		Lane
     */
    protected Lane create()
    {
        try
        {       
			this.lane = LaneBusinessDelegate.getLaneInstance().createLane( lane );
        }
        catch( Throwable exc )
        {
        	LOGGER.info( "LaneController:create() - exception Lane - " + exc.getMessage());        	
        	return null;
        }
        
        return this.lane;
    }

    /**
     * Handles updating a Lane BO
     * @return		Lane
     */    
    protected Lane update()
    {
    	// store provided data
        Lane tmp = lane;

        // load actual data from db
    	load();
    	
    	// copy provided data into actual data
    	lane.copyShallow( tmp );
    	
        try
        {                        	        
			// create the LaneBusiness Delegate            
			LaneBusinessDelegate delegate = LaneBusinessDelegate.getLaneInstance();
            this.lane = delegate.saveLane( lane );
            
            if ( this.lane != null )
                LOGGER.info( "LaneController:update() - successfully updated Lane - " + lane.toString() );
        }
        catch( Throwable exc )
        {
        	LOGGER.info( "LaneController:update() - successfully update Lane - " + exc.getMessage());        	
        	return null;
        }
        
        return this.lane;
        
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

    protected Lane load()
    {
    	return( load( new Long( lane.getLanePrimaryKey().getFirstKey().toString() ) ));
    }

//************************************************************************    
// Attributes
//************************************************************************
    protected Lane lane = null;
    private static final Logger LOGGER = Logger.getLogger(Lane.class.getName());
    
}


