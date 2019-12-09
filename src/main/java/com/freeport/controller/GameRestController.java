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
 * Implements Struts action processing for business entity Game.
 *
 * @author Dev Team
 */
@RestController
@RequestMapping("/Game")
public class GameRestController extends BaseSpringRestController
{

    /**
     * Handles saving a Game BO.  if not key provided, calls create, otherwise calls save
     * @param		Game game
     * @return		Game
     */
	@RequestMapping("/save")
    public Game save( @RequestBody Game game )
    {
    	// assign locally
    	this.game = game;
    	
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
     * Handles deleting a Game BO
     * @param		Long gameId
     * @param 		Long[] childIds
     * @return		boolean
     */
    @RequestMapping("/delete")    
    public boolean delete( @RequestParam(value="game.gameId", required=false) Long gameId, 
    						@RequestParam(value="childIds", required=false) Long[] childIds )
    {                
        try
        {
        	GameBusinessDelegate delegate = GameBusinessDelegate.getGameInstance();
        	
        	if ( childIds == null || childIds.length == 0 )
        	{
        		delegate.delete( new GamePrimaryKey( gameId ) );
        		LOGGER.info( "GameController:delete() - successfully deleted Game with key " + game.getGamePrimaryKey().valuesAsCollection() );
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
	                	LOGGER.info( "GameController:delete() - " + exc.getMessage() );
	                	return false;
	                }
        		}
        	}
        }
        catch( Throwable exc )
        {
        	LOGGER.info( "GameController:delete() - " + exc.getMessage() );
        	return false;        	
        }
        
        return true;
	}        
	
    /**
     * Handles loading a Game BO
     * @param		Long gameId
     * @return		Game
     */    
    @RequestMapping("/load")
    public Game load( @RequestParam(value="game.gameId", required=true) Long gameId )
    {    	
        GamePrimaryKey pk = null;

    	try
        {  
    		System.out.println( "\n\n****Game.load pk is " + gameId );
        	if ( gameId != null )
        	{
        		pk = new GamePrimaryKey( gameId );
        		
        		// load the Game
	            this.game = GameBusinessDelegate.getGameInstance().getGame( pk );
	            
	            LOGGER.info( "GameController:load() - successfully loaded - " + this.game.toString() );             
			}
			else
			{
	            LOGGER.info( "GameController:load() - unable to locate the primary key as an attribute or a selection for - " + game.toString() );
	            return null;
			}	            
        }
        catch( Throwable exc )
        {
            LOGGER.info( "GameController:load() - failed to load Game using Id " + gameId + ", " + exc.getMessage() );
            return null;
        }

        return game;

    }

    /**
     * Handles loading all Game business objects
     * @return		List<Game>
     */
    @RequestMapping("/loadAll")
    public List<Game> loadAll()
    {                
        List<Game> gameList = null;
        
    	try
        {                        
            // load the Game
            gameList = GameBusinessDelegate.getGameInstance().getAllGame();
            
            if ( gameList != null )
                LOGGER.info(  "GameController:loadAllGame() - successfully loaded all Games" );
        }
        catch( Throwable exc )
        {
            LOGGER.info(  "GameController:loadAll() - failed to load all Games - " + exc.getMessage() );
        	return null;
            
        }

        return gameList;
                            
    }


// findAllBy methods


    /**
     * save Player on Game
     * @param		Long	gameId
     * @param		Long	childId
     * @param		Game game
     * @return		Game
     */     
	@RequestMapping("/savePlayer")
	public Game savePlayer( @RequestParam(value="game.gameId", required=true) Long gameId, 
											@RequestParam(value="childIds", required=true) Long childId, @RequestBody Game game )
	{
		if ( load( gameId ) == null )
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
            	LOGGER.info( "GameController:savePlayer() failed to get Player using id " + childId + " - " + exc.getMessage() );
            	return null;
            }
	
			game.setPlayer( child );
		
			try
			{
				// save it
				parentDelegate.saveGame( game );
			}
			catch( Exception exc )
			{
				LOGGER.info( "GameController:savePlayer() failed saving parent Game - " + exc.getMessage() );
			}
		}
		
		return game;
	}

    /**
     * delete Player on Game
     * @param		Long gameId
     * @return		Game
     */     
	@RequestMapping("/deletePlayer")
	public Game deletePlayer( @RequestParam(value="game.gameId", required=true) Long gameId )
	
	{
		if ( load( gameId ) == null )
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
				LOGGER.info( "GameController:deletePlayer() failed to save Game - " + exc.getMessage() );
			}
			
			try
			{
				// safe to delete the child			
				PlayerBusinessDelegate childDelegate = PlayerBusinessDelegate.getPlayerInstance();
				childDelegate.delete( pk );
			}
			catch( Exception exc )
			{
				LOGGER.info( "GameController:deletePlayer() failed  - " + exc.getMessage() );
			}
		}
		
		return game;
	}
	


    /**
     * Handles creating a Game BO
     * @return		Game
     */
    protected Game create()
    {
        try
        {       
			this.game = GameBusinessDelegate.getGameInstance().createGame( game );
        }
        catch( Throwable exc )
        {
        	LOGGER.info( "GameController:create() - exception Game - " + exc.getMessage());        	
        	return null;
        }
        
        return this.game;
    }

    /**
     * Handles updating a Game BO
     * @return		Game
     */    
    protected Game update()
    {
    	// store provided data
        Game tmp = game;

        // load actual data from db
    	load();
    	
    	// copy provided data into actual data
    	game.copyShallow( tmp );
    	
        try
        {                        	        
			// create the GameBusiness Delegate            
			GameBusinessDelegate delegate = GameBusinessDelegate.getGameInstance();
            this.game = delegate.saveGame( game );
            
            if ( this.game != null )
                LOGGER.info( "GameController:update() - successfully updated Game - " + game.toString() );
        }
        catch( Throwable exc )
        {
        	LOGGER.info( "GameController:update() - successfully update Game - " + exc.getMessage());        	
        	return null;
        }
        
        return this.game;
        
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

    protected Game load()
    {
    	return( load( new Long( game.getGamePrimaryKey().getFirstKey().toString() ) ));
    }

//************************************************************************    
// Attributes
//************************************************************************
    protected Game game = null;
    private static final Logger LOGGER = Logger.getLogger(Game.class.getName());
    
}


