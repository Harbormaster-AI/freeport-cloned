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
 * Implements Struts action processing for business entity Player.
 *
 * @author Dev Team
 */

public class PlayerRestService extends BaseRestService
{

	public PlayerRestService()
	{}
	
    /**
     * Handles saving a Player BO.  if not key provided, calls create, otherwise calls save
     * @exception	ProcessingException
     */
    protected Player save()
    	throws ProcessingException
    {
		// doing it here helps
		getPlayer();

		LOGGER.info( "Player.save() on - " + player );
		
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
     * Returns true if the player is non-null and has it's primary key field(s) set
     * @return		boolean
     */
    protected boolean hasPrimaryKey()
    {
    	boolean hasPK = false;

		if ( player != null && player.getPlayerPrimaryKey().hasBeenAssigned() == true )
		   hasPK = true;
		
		return( hasPK );
    }

    /**
     * Handles updating a Player BO
     * @return		Player
     * @exception	ProcessingException
     */    
    protected Player update()
    	throws ProcessingException
    {
    	// store provided data
        Player tmp = player;

        // load actual data from storage
        loadHelper( player.getPlayerPrimaryKey() );
    	
    	// copy provided data into actual data
    	player.copyShallow( tmp );
    	
        try
        {                        	        
			// create the PlayerBusiness Delegate            
			PlayerBusinessDelegate delegate = PlayerBusinessDelegate.getPlayerInstance();
            this.player = delegate.savePlayer( player );
            
            if ( this.player != null )
            	LOGGER.info( "PlayerRestService:update() - successfully updated Player - " + player.toString() );
        }
        catch( Throwable exc )
        {
        	signalBadRequest();
        	
        	String errMsg = "PlayerRestService:update() - successfully update Player - " + exc.getMessage();
        	LOGGER.severe( errMsg );
        	throw new ProcessingException( errMsg );
        }
        
        return this.player;
        
    }

    /**
     * Handles creating a Player BO
     * @return		Player
     */
    protected Player create()
    	throws ProcessingException
    {
        try
        {       
        	player 		= getPlayer();
			this.player 	= PlayerBusinessDelegate.getPlayerInstance().createPlayer( player );
        }
        catch( Throwable exc )
        {
        	signalBadRequest();
        	
        	String errMsg = "PlayerRestService:create() - exception Player - " + exc.getMessage();
        	LOGGER.severe( errMsg );
        	throw new ProcessingException( errMsg );
        }
        
        return this.player;
    }


    
    /**
     * Handles deleting a Player BO
     * @exception	ProcessingException
     */
    protected void delete() 
    	throws ProcessingException
    {                
        try
        {
        	PlayerBusinessDelegate delegate = PlayerBusinessDelegate.getPlayerInstance();

        	Long[] childIds = getChildIds();
        	
        	if ( childIds == null || childIds.length == 0 )
        	{
        		Long playerId  = parseId( "playerId" );
        		delegate.delete( new PlayerPrimaryKey( playerId  ) );
        		LOGGER.info( "PlayerRestService:delete() - successfully deleted Player with key " + player.getPlayerPrimaryKey().valuesAsCollection() );
        	}
        	else
        	{
        		for ( Long id : childIds )
        		{
        			try
        			{
        				delegate.delete( new PlayerPrimaryKey( id ) );
        			}
	                catch( Throwable exc )
	                {
	                	signalBadRequest();

	                	String errMsg = "PlayerRestService:delete() - " + exc.getMessage();
	                	LOGGER.severe( errMsg );
	                	throw new ProcessingException( errMsg );
	                }
        		}
        	}
        }
        catch( Throwable exc )
        {
        	signalBadRequest();
        	String errMsg = "PlayerRestService:delete() - " + exc.getMessage();
        	LOGGER.severe( errMsg );
        	throw new ProcessingException( errMsg );
        }
	}        
	
    /**
     * Handles loading a Player BO
     * @param		Long playerId
     * @exception	ProcessingException
     * @return		Player
     */    
    protected Player load() 
    	throws ProcessingException
    {    	
        PlayerPrimaryKey pk 	= null;
		Long playerId  = parseId( "playerId" );

    	try
        {
    		LOGGER.info( "Player.load pk is " + playerId );
    		
        	if ( playerId != null )
        	{
        		pk = new PlayerPrimaryKey( playerId );

        		loadHelper( pk );

        		// load the contained instance of Player
	            this.player = PlayerBusinessDelegate.getPlayerInstance().getPlayer( pk );
	            
	            LOGGER.info( "PlayerRestService:load() - successfully loaded - " + this.player.toString() );             
			}
			else
			{
	        	signalBadRequest();

				String errMsg = "PlayerRestService:load() - unable to locate the primary key as an attribute or a selection for - " + player.toString();				
				LOGGER.severe( errMsg );
	            throw new ProcessingException( errMsg );
			}	            
        }
        catch( Throwable exc )
        {
        	signalBadRequest();

        	String errMsg = "PlayerRestService:load() - failed to load Player using Id " + playerId + ", " + exc.getMessage();				
			LOGGER.severe( errMsg );
            throw new ProcessingException( errMsg );
        }

        return player;

    }

    /**
     * Handles loading all Player business objects
     * @return		List<Player>
     * @exception	ProcessingException
     */
    protected List<Player> loadAll()
    	throws ProcessingException
    {                
        List<Player> playerList = null;
        
    	try
        {                        
            // load the Player
            playerList = PlayerBusinessDelegate.getPlayerInstance().getAllPlayer();
            
            if ( playerList != null )
            	LOGGER.info(  "PlayerRestService:loadAllPlayer() - successfully loaded all Players" );
        }
        catch( Throwable exc )
        {
        	signalBadRequest();

        	String errMsg = "PlayerRestService:loadAll() - failed to load all Players - " + exc.getMessage();				
			LOGGER.severe( errMsg );
            throw new ProcessingException( errMsg );            
        }

        return playerList;
                            
    }






    protected Player loadHelper( PlayerPrimaryKey pk )
    		throws ProcessingException
    {
    	try
        {
    		LOGGER.info( "Player.loadHelper primary key is " + pk);
    		
        	if ( pk != null )
        	{
        		// load the contained instance of Player
	            this.player = PlayerBusinessDelegate.getPlayerInstance().getPlayer( pk );
	            
	            LOGGER.info( "PlayerRestService:loadHelper() - successfully loaded - " + this.player.toString() );             
			}
			else
			{
	        	signalBadRequest();

				String errMsg = "PlayerRestService:loadHelper() - null primary key provided.";				
				LOGGER.severe( errMsg );
	            throw new ProcessingException( errMsg );
			}	            
        }
        catch( Throwable exc )
        {
        	signalBadRequest();

        	String errMsg = "PlayerRestService:load() - failed to load Player using pk " + pk + ", " + exc.getMessage();				
			LOGGER.severe( errMsg );
            throw new ProcessingException( errMsg );
        }

        return player;

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
            throw new ProcessingException("Player.execute(...) - unable to handle action " + action);
		}
		
		return returnVal;
	}
	
	/**
	 * Uses ObjectMapper to map from Json to a Player. Found in the request body.
	 * 
	 * @return Player
	 */
	private Player getPlayer()
	{
		if ( player == null )
		{
			try {
				ObjectMapper mapper = new ObjectMapper();
				mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
				player = mapper.readValue(java.net.URLDecoder.decode(request.queryString(),"UTF-8"), Player.class);
				
	        } catch (Exception exc) 
	        {
	            signalBadRequest();
	            LOGGER.severe( "PlayerRestService.getPlayer() - failed to Json map from String to Player - " + exc.getMessage() );
	        }			
		}
		return( player );
	}
	
	protected String getSubclassName()
	{ return( "PlayerRestService" ); }
	
//************************************************************************    
// Attributes
//************************************************************************
    private Player player 			= null;
    private static final Logger LOGGER 	= Logger.getLogger(BaseRestService.class.getName());
    
}
