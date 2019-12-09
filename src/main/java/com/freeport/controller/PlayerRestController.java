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
 * Implements Struts action processing for business entity Player.
 *
 * @author Dev Team
 */
@RestController
@RequestMapping("/Player")
public class PlayerRestController extends BaseSpringRestController
{

    /**
     * Handles saving a Player BO.  if not key provided, calls create, otherwise calls save
     * @param		Player player
     * @return		Player
     */
	@RequestMapping("/save")
    public Player save( @RequestBody Player player )
    {
    	// assign locally
    	this.player = player;
    	
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
     * Handles deleting a Player BO
     * @param		Long playerId
     * @param 		Long[] childIds
     * @return		boolean
     */
    @RequestMapping("/delete")    
    public boolean delete( @RequestParam(value="player.playerId", required=false) Long playerId, 
    						@RequestParam(value="childIds", required=false) Long[] childIds )
    {                
        try
        {
        	PlayerBusinessDelegate delegate = PlayerBusinessDelegate.getPlayerInstance();
        	
        	if ( childIds == null || childIds.length == 0 )
        	{
        		delegate.delete( new PlayerPrimaryKey( playerId ) );
        		LOGGER.info( "PlayerController:delete() - successfully deleted Player with key " + player.getPlayerPrimaryKey().valuesAsCollection() );
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
	                	LOGGER.info( "PlayerController:delete() - " + exc.getMessage() );
	                	return false;
	                }
        		}
        	}
        }
        catch( Throwable exc )
        {
        	LOGGER.info( "PlayerController:delete() - " + exc.getMessage() );
        	return false;        	
        }
        
        return true;
	}        
	
    /**
     * Handles loading a Player BO
     * @param		Long playerId
     * @return		Player
     */    
    @RequestMapping("/load")
    public Player load( @RequestParam(value="player.playerId", required=true) Long playerId )
    {    	
        PlayerPrimaryKey pk = null;

    	try
        {  
    		System.out.println( "\n\n****Player.load pk is " + playerId );
        	if ( playerId != null )
        	{
        		pk = new PlayerPrimaryKey( playerId );
        		
        		// load the Player
	            this.player = PlayerBusinessDelegate.getPlayerInstance().getPlayer( pk );
	            
	            LOGGER.info( "PlayerController:load() - successfully loaded - " + this.player.toString() );             
			}
			else
			{
	            LOGGER.info( "PlayerController:load() - unable to locate the primary key as an attribute or a selection for - " + player.toString() );
	            return null;
			}	            
        }
        catch( Throwable exc )
        {
            LOGGER.info( "PlayerController:load() - failed to load Player using Id " + playerId + ", " + exc.getMessage() );
            return null;
        }

        return player;

    }

    /**
     * Handles loading all Player business objects
     * @return		List<Player>
     */
    @RequestMapping("/loadAll")
    public List<Player> loadAll()
    {                
        List<Player> playerList = null;
        
    	try
        {                        
            // load the Player
            playerList = PlayerBusinessDelegate.getPlayerInstance().getAllPlayer();
            
            if ( playerList != null )
                LOGGER.info(  "PlayerController:loadAllPlayer() - successfully loaded all Players" );
        }
        catch( Throwable exc )
        {
            LOGGER.info(  "PlayerController:loadAll() - failed to load all Players - " + exc.getMessage() );
        	return null;
            
        }

        return playerList;
                            
    }


// findAllBy methods




    /**
     * Handles creating a Player BO
     * @return		Player
     */
    protected Player create()
    {
        try
        {       
			this.player = PlayerBusinessDelegate.getPlayerInstance().createPlayer( player );
        }
        catch( Throwable exc )
        {
        	LOGGER.info( "PlayerController:create() - exception Player - " + exc.getMessage());        	
        	return null;
        }
        
        return this.player;
    }

    /**
     * Handles updating a Player BO
     * @return		Player
     */    
    protected Player update()
    {
    	// store provided data
        Player tmp = player;

        // load actual data from db
    	load();
    	
    	// copy provided data into actual data
    	player.copyShallow( tmp );
    	
        try
        {                        	        
			// create the PlayerBusiness Delegate            
			PlayerBusinessDelegate delegate = PlayerBusinessDelegate.getPlayerInstance();
            this.player = delegate.savePlayer( player );
            
            if ( this.player != null )
                LOGGER.info( "PlayerController:update() - successfully updated Player - " + player.toString() );
        }
        catch( Throwable exc )
        {
        	LOGGER.info( "PlayerController:update() - successfully update Player - " + exc.getMessage());        	
        	return null;
        }
        
        return this.player;
        
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

    protected Player load()
    {
    	return( load( new Long( player.getPlayerPrimaryKey().getFirstKey().toString() ) ));
    }

//************************************************************************    
// Attributes
//************************************************************************
    protected Player player = null;
    private static final Logger LOGGER = Logger.getLogger(Player.class.getName());
    
}


