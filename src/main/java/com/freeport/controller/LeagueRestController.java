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
 * Implements Struts action processing for business entity League.
 *
 * @author Dev Team
 */
@RestController
@RequestMapping("/League")
public class LeagueRestController extends BaseSpringRestController
{

    /**
     * Handles saving a League BO.  if not key provided, calls create, otherwise calls save
     * @param		League league
     * @return		League
     */
	@RequestMapping("/save")
    public League save( @RequestBody League league )
    {
    	// assign locally
    	this.league = league;
    	
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
     * Handles deleting a League BO
     * @param		Long leagueId
     * @param 		Long[] childIds
     * @return		boolean
     */
    @RequestMapping("/delete")    
    public boolean delete( @RequestParam(value="league.leagueId", required=false) Long leagueId, 
    						@RequestParam(value="childIds", required=false) Long[] childIds )
    {                
        try
        {
        	LeagueBusinessDelegate delegate = LeagueBusinessDelegate.getLeagueInstance();
        	
        	if ( childIds == null || childIds.length == 0 )
        	{
        		delegate.delete( new LeaguePrimaryKey( leagueId ) );
        		LOGGER.info( "LeagueController:delete() - successfully deleted League with key " + league.getLeaguePrimaryKey().valuesAsCollection() );
        	}
        	else
        	{
        		for ( Long id : childIds )
        		{
        			try
        			{
        				delegate.delete( new LeaguePrimaryKey( id ) );
        			}
	                catch( Throwable exc )
	                {
	                	LOGGER.info( "LeagueController:delete() - " + exc.getMessage() );
	                	return false;
	                }
        		}
        	}
        }
        catch( Throwable exc )
        {
        	LOGGER.info( "LeagueController:delete() - " + exc.getMessage() );
        	return false;        	
        }
        
        return true;
	}        
	
    /**
     * Handles loading a League BO
     * @param		Long leagueId
     * @return		League
     */    
    @RequestMapping("/load")
    public League load( @RequestParam(value="league.leagueId", required=true) Long leagueId )
    {    	
        LeaguePrimaryKey pk = null;

    	try
        {  
    		System.out.println( "\n\n****League.load pk is " + leagueId );
        	if ( leagueId != null )
        	{
        		pk = new LeaguePrimaryKey( leagueId );
        		
        		// load the League
	            this.league = LeagueBusinessDelegate.getLeagueInstance().getLeague( pk );
	            
	            LOGGER.info( "LeagueController:load() - successfully loaded - " + this.league.toString() );             
			}
			else
			{
	            LOGGER.info( "LeagueController:load() - unable to locate the primary key as an attribute or a selection for - " + league.toString() );
	            return null;
			}	            
        }
        catch( Throwable exc )
        {
            LOGGER.info( "LeagueController:load() - failed to load League using Id " + leagueId + ", " + exc.getMessage() );
            return null;
        }

        return league;

    }

    /**
     * Handles loading all League business objects
     * @return		List<League>
     */
    @RequestMapping("/loadAll")
    public List<League> loadAll()
    {                
        List<League> leagueList = null;
        
    	try
        {                        
            // load the League
            leagueList = LeagueBusinessDelegate.getLeagueInstance().getAllLeague();
            
            if ( leagueList != null )
                LOGGER.info(  "LeagueController:loadAllLeague() - successfully loaded all Leagues" );
        }
        catch( Throwable exc )
        {
            LOGGER.info(  "LeagueController:loadAll() - failed to load all Leagues - " + exc.getMessage() );
        	return null;
            
        }

        return leagueList;
                            
    }


// findAllBy methods



    /**
     * save Players on League
     * @param		Long leagueId
     * @param		Long childId
     * @param		Long[] childIds
     * @return		League
     */     
	@RequestMapping("/savePlayers")
	public League savePlayers( @RequestParam(value="league.leagueId", required=false) Long leagueId, 
											@RequestParam(value="childIds", required=false) Long childId, @RequestParam("") Long[] childIds )
	{
		if ( load( leagueId ) == null )
			return( null );
		
		PlayerPrimaryKey pk 					= null;
		Player child							= null;
		PlayerBusinessDelegate childDelegate 	= PlayerBusinessDelegate.getPlayerInstance();
		LeagueBusinessDelegate parentDelegate = LeagueBusinessDelegate.getLeagueInstance();		
		
		if ( childId != null || childIds.length == 0 )// creating or saving one
		{
			pk = new PlayerPrimaryKey( childId );
			
			try
			{
				// find the Player
				child = childDelegate.getPlayer( pk );
			}
			catch( Exception exc )
			{
				LOGGER.info( "LeagueController:savePlayers() failed get child Player using id " + childId  + "- " + exc.getMessage() );
				return( null );
			}
			
			// add it to the Players 
			league.getPlayers().add( child );				
		}
		else
		{
			// clear out the Players but 
			league.getPlayers().clear();
			
			// finally, find each child and add it
			if ( childIds != null )
			{
				for( Long id : childIds )
				{
					pk = new PlayerPrimaryKey( id );
					try
					{
						// find the Player
						child = childDelegate.getPlayer( pk );
						// add it to the Players List
						league.getPlayers().add( child );
					}
					catch( Exception exc )
					{
						LOGGER.info( "LeagueController:savePlayers() failed get child Player using id " + id  + "- " + exc.getMessage() );
					}
				}
			}
		}

		try
		{
			// save the League
			parentDelegate.saveLeague( league );
		}
		catch( Exception exc )
		{
			LOGGER.info( "LeagueController:savePlayers() failed saving parent League - " + exc.getMessage() );
		}

		return league;
	}

    /**
     * delete Players on League
     * @param		Long leagueId
     * @param		Long[] childIds
     * @return		League
     */     	
	@RequestMapping("/deletePlayers")
	public League deletePlayers( @RequestParam(value="league.leagueId", required=true) Long leagueId, 
											@RequestParam(value="childIds", required=false) Long[] childIds )
	{		
		if ( load( leagueId ) == null )
			return( null );

		if ( childIds != null || childIds.length == 0 )
		{
			PlayerPrimaryKey pk 					= null;
			PlayerBusinessDelegate childDelegate 	= PlayerBusinessDelegate.getPlayerInstance();
			LeagueBusinessDelegate parentDelegate = LeagueBusinessDelegate.getLeagueInstance();
			Set<Player> children					= league.getPlayers();
			Player child 							= null;
			
			for( Long id : childIds )
			{
				try
				{
					pk = new PlayerPrimaryKey( id );
					
					// first remove the relevant child from the list
					child = childDelegate.getPlayer( pk );
					children.remove( child );
					
					// then safe to delete the child				
					childDelegate.delete( pk );
				}
				catch( Exception exc )
				{
					LOGGER.info( "LeagueController:deletePlayers() failed - " + exc.getMessage() );
				}
			}
			
			// assign the modified list of Player back to the league
			league.setPlayers( children );			
			// save it 
			try
			{
				league = parentDelegate.saveLeague( league );
			}
			catch( Throwable exc )
			{
				LOGGER.info( "LeagueController:deletePlayers() failed to save the League - " + exc.getMessage() );
			}
		}
		
		return league;
	}


    /**
     * Handles creating a League BO
     * @return		League
     */
    protected League create()
    {
        try
        {       
			this.league = LeagueBusinessDelegate.getLeagueInstance().createLeague( league );
        }
        catch( Throwable exc )
        {
        	LOGGER.info( "LeagueController:create() - exception League - " + exc.getMessage());        	
        	return null;
        }
        
        return this.league;
    }

    /**
     * Handles updating a League BO
     * @return		League
     */    
    protected League update()
    {
    	// store provided data
        League tmp = league;

        // load actual data from db
    	load();
    	
    	// copy provided data into actual data
    	league.copyShallow( tmp );
    	
        try
        {                        	        
			// create the LeagueBusiness Delegate            
			LeagueBusinessDelegate delegate = LeagueBusinessDelegate.getLeagueInstance();
            this.league = delegate.saveLeague( league );
            
            if ( this.league != null )
                LOGGER.info( "LeagueController:update() - successfully updated League - " + league.toString() );
        }
        catch( Throwable exc )
        {
        	LOGGER.info( "LeagueController:update() - successfully update League - " + exc.getMessage());        	
        	return null;
        }
        
        return this.league;
        
    }


    /**
     * Returns true if the league is non-null and has it's primary key field(s) set
     * @return		boolean
     */
    protected boolean hasPrimaryKey()
    {
    	boolean hasPK = false;

		if ( league != null && league.getLeaguePrimaryKey().hasBeenAssigned() == true )
		   hasPK = true;
		
		return( hasPK );
    }

    protected League load()
    {
    	return( load( new Long( league.getLeaguePrimaryKey().getFirstKey().toString() ) ));
    }

//************************************************************************    
// Attributes
//************************************************************************
    protected League league = null;
    private static final Logger LOGGER = Logger.getLogger(League.class.getName());
    
}


