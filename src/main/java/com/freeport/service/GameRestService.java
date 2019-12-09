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
 * Implements Struts action processing for business entity Game.
 *
 * @author Dev Team
 */

public class GameRestService extends BaseRestService
{

	public GameRestService()
	{}
	
    /**
     * Handles saving a Game BO.  if not key provided, calls create, otherwise calls save
     * @exception	ProcessingException
     */
    protected Game save()
    	throws ProcessingException
    {
		// doing it here helps
		getGame();

		LOGGER.info( "Game.save() on - " + game );
		
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
     * Returns true if the game is non-null and has it's primary key field(s) set
     * @return		boolean
     */
    protected boolean hasPrimaryKey()
    {
    	boolean hasPK = false;

		if ( game != null && game.getGamePrimaryKey().hasBeenAssigned() == true )
		   hasPK = true;
		
		return( hasPK );
    }

    /**
     * Handles updating a Game BO
     * @return		Game
     * @exception	ProcessingException
     */    
    protected Game update()
    	throws ProcessingException
    {
    	// store provided data
        Game tmp = game;

        // load actual data from storage
        loadHelper( game.getGamePrimaryKey() );
    	
    	// copy provided data into actual data
    	game.copyShallow( tmp );
    	
        try
        {                        	        
			// create the GameBusiness Delegate            
			GameBusinessDelegate delegate = GameBusinessDelegate.getGameInstance();
            this.game = delegate.saveGame( game );
            
            if ( this.game != null )
            	LOGGER.info( "GameRestService:update() - successfully updated Game - " + game.toString() );
        }
        catch( Throwable exc )
        {
        	signalBadRequest();
        	
        	String errMsg = "GameRestService:update() - successfully update Game - " + exc.getMessage();
        	LOGGER.severe( errMsg );
        	throw new ProcessingException( errMsg );
        }
        
        return this.game;
        
    }

    /**
     * Handles creating a Game BO
     * @return		Game
     */
    protected Game create()
    	throws ProcessingException
    {
        try
        {       
        	game 		= getGame();
			this.game 	= GameBusinessDelegate.getGameInstance().createGame( game );
        }
        catch( Throwable exc )
        {
        	signalBadRequest();
        	
        	String errMsg = "GameRestService:create() - exception Game - " + exc.getMessage();
        	LOGGER.severe( errMsg );
        	throw new ProcessingException( errMsg );
        }
        
        return this.game;
    }


    
    /**
     * Handles deleting a Game BO
     * @exception	ProcessingException
     */
    protected void delete() 
    	throws ProcessingException
    {                
        try
        {
        	GameBusinessDelegate delegate = GameBusinessDelegate.getGameInstance();

        	Long[] childIds = getChildIds();
        	
        	if ( childIds == null || childIds.length == 0 )
        	{
        		Long gameId  = parseId( "gameId" );
        		delegate.delete( new GamePrimaryKey( gameId  ) );
        		LOGGER.info( "GameRestService:delete() - successfully deleted Game with key " + game.getGamePrimaryKey().valuesAsCollection() );
        	}
        	else
        	{
        		for ( Long id : childIds )
        		{
        			try
        			{
        				delegate.delete( new GamePrimaryKey( id ) );
        			}
	                catch( Throwable exc )
	                {
	                	signalBadRequest();

	                	String errMsg = "GameRestService:delete() - " + exc.getMessage();
	                	LOGGER.severe( errMsg );
	                	throw new ProcessingException( errMsg );
	                }
        		}
        	}
        }
        catch( Throwable exc )
        {
        	signalBadRequest();
        	String errMsg = "GameRestService:delete() - " + exc.getMessage();
        	LOGGER.severe( errMsg );
        	throw new ProcessingException( errMsg );
        }
	}        
	
    /**
     * Handles loading a Game BO
     * @param		Long gameId
     * @exception	ProcessingException
     * @return		Game
     */    
    protected Game load() 
    	throws ProcessingException
    {    	
        GamePrimaryKey pk 	= null;
		Long gameId  = parseId( "gameId" );

    	try
        {
    		LOGGER.info( "Game.load pk is " + gameId );
    		
        	if ( gameId != null )
        	{
        		pk = new GamePrimaryKey( gameId );

        		loadHelper( pk );

        		// load the contained instance of Game
	            this.game = GameBusinessDelegate.getGameInstance().getGame( pk );
	            
	            LOGGER.info( "GameRestService:load() - successfully loaded - " + this.game.toString() );             
			}
			else
			{
	        	signalBadRequest();

				String errMsg = "GameRestService:load() - unable to locate the primary key as an attribute or a selection for - " + game.toString();				
				LOGGER.severe( errMsg );
	            throw new ProcessingException( errMsg );
			}	            
        }
        catch( Throwable exc )
        {
        	signalBadRequest();

        	String errMsg = "GameRestService:load() - failed to load Game using Id " + gameId + ", " + exc.getMessage();				
			LOGGER.severe( errMsg );
            throw new ProcessingException( errMsg );
        }

        return game;

    }

    /**
     * Handles loading all Game business objects
     * @return		List<Game>
     * @exception	ProcessingException
     */
    protected List<Game> loadAll()
    	throws ProcessingException
    {                
        List<Game> gameList = null;
        
    	try
        {                        
            // load the Game
            gameList = GameBusinessDelegate.getGameInstance().getAllGame();
            
            if ( gameList != null )
            	LOGGER.info(  "GameRestService:loadAllGame() - successfully loaded all Games" );
        }
        catch( Throwable exc )
        {
        	signalBadRequest();

        	String errMsg = "GameRestService:loadAll() - failed to load all Games - " + exc.getMessage();				
			LOGGER.severe( errMsg );
            throw new ProcessingException( errMsg );            
        }

        return gameList;
                            
    }



    /**
     * save Player on Game
     * @param		Game game
     * @return		Game
     * @exception	ProcessingException
     */     
	protected Game savePlayer()
		throws ProcessingException
	{
		Long gameId 	= parseId( "gameId" );
		Long childId 					= parseId( "childId" );
		
		if ( loadHelper( new GamePrimaryKey( gameId ) ) == null )
			return( null );
		
		if ( childId != null )
		{
			PlayerBusinessDelegate childDelegate 	= PlayerBusinessDelegate.getPlayerInstance();
			GameBusinessDelegate parentDelegate = GameBusinessDelegate.getGameInstance();			
			Player child 							= null;

			try
			{
				child = childDelegate.getPlayer( new PlayerPrimaryKey( childId ) );
			}
            catch( Throwable exc )
            {
            	signalBadRequest();

            	String errMsg = "GameRestService:savePlayer() failed to get Player using id " + childId + " - " + exc.getMessage();
            	LOGGER.severe( errMsg);
            	throw new ProcessingException( errMsg );
            }
	
			game.setPlayer( child );
		
			try
			{
				// save it
				parentDelegate.saveGame( game );
			}
			catch( Exception exc )
			{
	        	signalBadRequest();

				String errMsg = "GameRestService:savePlayer() failed saving parent Game - " + exc.getMessage();
				LOGGER.severe( errMsg );
				throw new ProcessingException( errMsg );
			}
		}
		
		return game;
	}

    /**
     * delete Player on Game
     * @return		Game
     * @exception	ProcessingException
     */     
	protected Game deletePlayer()
		throws ProcessingException
	{
		Long gameId = parseId( "gameId" );
		
 		if ( loadHelper( new GamePrimaryKey( gameId ) ) == null )
			return( null );

		if ( game.getPlayer() != null )
		{
			PlayerPrimaryKey pk = game.getPlayer().getPlayerPrimaryKey();
			
			// null out the parent first so there's no constraint during deletion
			game.setPlayer( null );
			try
			{
				GameBusinessDelegate parentDelegate = GameBusinessDelegate.getGameInstance();

				// save it
				game = parentDelegate.saveGame( game );
			}
			catch( Exception exc )
			{
	        	signalBadRequest();

				String errMsg = "GameRestService:deletePlayer() failed to save Game - " + exc.getMessage();
				LOGGER.severe( errMsg );
				throw new ProcessingException( errMsg );
			}
			
			try
			{
				// safe to delete the child			
				PlayerBusinessDelegate childDelegate = PlayerBusinessDelegate.getPlayerInstance();
				childDelegate.delete( pk );
			}
			catch( Exception exc )
			{
	        	signalBadRequest();

				String errMsg = "GameRestService:deletePlayer() failed  - " + exc.getMessage();
				LOGGER.severe( errMsg );
				throw new ProcessingException( errMsg );
			}
		}
		
		return game;
	}
	



    protected Game loadHelper( GamePrimaryKey pk )
    		throws ProcessingException
    {
    	try
        {
    		LOGGER.info( "Game.loadHelper primary key is " + pk);
    		
        	if ( pk != null )
        	{
        		// load the contained instance of Game
	            this.game = GameBusinessDelegate.getGameInstance().getGame( pk );
	            
	            LOGGER.info( "GameRestService:loadHelper() - successfully loaded - " + this.game.toString() );             
			}
			else
			{
	        	signalBadRequest();

				String errMsg = "GameRestService:loadHelper() - null primary key provided.";				
				LOGGER.severe( errMsg );
	            throw new ProcessingException( errMsg );
			}	            
        }
        catch( Throwable exc )
        {
        	signalBadRequest();

        	String errMsg = "GameRestService:load() - failed to load Game using pk " + pk + ", " + exc.getMessage();				
			LOGGER.severe( errMsg );
            throw new ProcessingException( errMsg );
        }

        return game;

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
 	        case "savePlayer":
	        	returnVal = savePlayer().getPlayer();
	        	break;
	        case "deletePlayer":
	        	returnVal = deletePlayer().getPlayer();
	        	break;
	        case "loadPlayer" :
	        	returnVal = load().getPlayer();
	        	break;
         default:
        	signalBadRequest();
            throw new ProcessingException("Game.execute(...) - unable to handle action " + action);
		}
		
		return returnVal;
	}
	
	/**
	 * Uses ObjectMapper to map from Json to a Game. Found in the request body.
	 * 
	 * @return Game
	 */
	private Game getGame()
	{
		if ( game == null )
		{
			try {
				ObjectMapper mapper = new ObjectMapper();
				mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
				game = mapper.readValue(java.net.URLDecoder.decode(request.queryString(),"UTF-8"), Game.class);
				
	        } catch (Exception exc) 
	        {
	            signalBadRequest();
	            LOGGER.severe( "GameRestService.getGame() - failed to Json map from String to Game - " + exc.getMessage() );
	        }			
		}
		return( game );
	}
	
	protected String getSubclassName()
	{ return( "GameRestService" ); }
	
//************************************************************************    
// Attributes
//************************************************************************
    private Game game 			= null;
    private static final Logger LOGGER 	= Logger.getLogger(BaseRestService.class.getName());
    
}
