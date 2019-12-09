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
 * Implements Struts action processing for business entity League.
 *
 * @author Dev Team
 */

public class LeagueRestService extends BaseRestService
{

	public LeagueRestService()
	{}
	
    /**
     * Handles saving a League BO.  if not key provided, calls create, otherwise calls save
     * @exception	ProcessingException
     */
    protected League save()
    	throws ProcessingException
    {
		// doing it here helps
		getLeague();

		LOGGER.info( "League.save() on - " + league );
		
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

    /**
     * Handles updating a League BO
     * @return		League
     * @exception	ProcessingException
     */    
    protected League update()
    	throws ProcessingException
    {
    	// store provided data
        League tmp = league;

        // load actual data from storage
        loadHelper( league.getLeaguePrimaryKey() );
    	
    	// copy provided data into actual data
    	league.copyShallow( tmp );
    	
        try
        {                        	        
			// create the LeagueBusiness Delegate            
			LeagueBusinessDelegate delegate = LeagueBusinessDelegate.getLeagueInstance();
            this.league = delegate.saveLeague( league );
            
            if ( this.league != null )
            	LOGGER.info( "LeagueRestService:update() - successfully updated League - " + league.toString() );
        }
        catch( Throwable exc )
        {
        	signalBadRequest();
        	
        	String errMsg = "LeagueRestService:update() - successfully update League - " + exc.getMessage();
        	LOGGER.severe( errMsg );
        	throw new ProcessingException( errMsg );
        }
        
        return this.league;
        
    }

    /**
     * Handles creating a League BO
     * @return		League
     */
    protected League create()
    	throws ProcessingException
    {
        try
        {       
        	league 		= getLeague();
			this.league 	= LeagueBusinessDelegate.getLeagueInstance().createLeague( league );
        }
        catch( Throwable exc )
        {
        	signalBadRequest();
        	
        	String errMsg = "LeagueRestService:create() - exception League - " + exc.getMessage();
        	LOGGER.severe( errMsg );
        	throw new ProcessingException( errMsg );
        }
        
        return this.league;
    }


    
    /**
     * Handles deleting a League BO
     * @exception	ProcessingException
     */
    protected void delete() 
    	throws ProcessingException
    {                
        try
        {
        	LeagueBusinessDelegate delegate = LeagueBusinessDelegate.getLeagueInstance();

        	Long[] childIds = getChildIds();
        	
        	if ( childIds == null || childIds.length == 0 )
        	{
        		Long leagueId  = parseId( "leagueId" );
        		delegate.delete( new LeaguePrimaryKey( leagueId  ) );
        		LOGGER.info( "LeagueRestService:delete() - successfully deleted League with key " + league.getLeaguePrimaryKey().valuesAsCollection() );
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
	                	signalBadRequest();

	                	String errMsg = "LeagueRestService:delete() - " + exc.getMessage();
	                	LOGGER.severe( errMsg );
	                	throw new ProcessingException( errMsg );
	                }
        		}
        	}
        }
        catch( Throwable exc )
        {
        	signalBadRequest();
        	String errMsg = "LeagueRestService:delete() - " + exc.getMessage();
        	LOGGER.severe( errMsg );
        	throw new ProcessingException( errMsg );
        }
	}        
	
    /**
     * Handles loading a League BO
     * @param		Long leagueId
     * @exception	ProcessingException
     * @return		League
     */    
    protected League load() 
    	throws ProcessingException
    {    	
        LeaguePrimaryKey pk 	= null;
		Long leagueId  = parseId( "leagueId" );

    	try
        {
    		LOGGER.info( "League.load pk is " + leagueId );
    		
        	if ( leagueId != null )
        	{
        		pk = new LeaguePrimaryKey( leagueId );

        		loadHelper( pk );

        		// load the contained instance of League
	            this.league = LeagueBusinessDelegate.getLeagueInstance().getLeague( pk );
	            
	            LOGGER.info( "LeagueRestService:load() - successfully loaded - " + this.league.toString() );             
			}
			else
			{
	        	signalBadRequest();

				String errMsg = "LeagueRestService:load() - unable to locate the primary key as an attribute or a selection for - " + league.toString();				
				LOGGER.severe( errMsg );
	            throw new ProcessingException( errMsg );
			}	            
        }
        catch( Throwable exc )
        {
        	signalBadRequest();

        	String errMsg = "LeagueRestService:load() - failed to load League using Id " + leagueId + ", " + exc.getMessage();				
			LOGGER.severe( errMsg );
            throw new ProcessingException( errMsg );
        }

        return league;

    }

    /**
     * Handles loading all League business objects
     * @return		List<League>
     * @exception	ProcessingException
     */
    protected List<League> loadAll()
    	throws ProcessingException
    {                
        List<League> leagueList = null;
        
    	try
        {                        
            // load the League
            leagueList = LeagueBusinessDelegate.getLeagueInstance().getAllLeague();
            
            if ( leagueList != null )
            	LOGGER.info(  "LeagueRestService:loadAllLeague() - successfully loaded all Leagues" );
        }
        catch( Throwable exc )
        {
        	signalBadRequest();

        	String errMsg = "LeagueRestService:loadAll() - failed to load all Leagues - " + exc.getMessage();				
			LOGGER.severe( errMsg );
            throw new ProcessingException( errMsg );            
        }

        return leagueList;
                            
    }




    /**
     * save Players on League
     * @param		Long leagueId
     * @param		Long childId
     * @param		String[] childIds
     * @return		League
     * @exception	ProcessingException
     */     
	protected League savePlayers()
		throws ProcessingException
	{
		Long leagueId = parseId( "leagueId" );
		Long childId = parseId( "childId" );
		
		if ( loadHelper( new LeaguePrimaryKey( leagueId ) ) == null )
			throw new ProcessingException( "League.savePlayers() - failed to load parent using Id " + leagueId );
		 
		PlayerPrimaryKey pk 					= null;
		Player child							= null;
		List<Player> childList				= null;
		PlayerBusinessDelegate childDelegate 	= PlayerBusinessDelegate.getPlayerInstance();
		LeagueBusinessDelegate parentDelegate = LeagueBusinessDelegate.getLeagueInstance();		
		
		Long[] childIds = getChildIds();
		
		if ( childId != null || childIds.length == 0 )// creating or saving one
		{
			pk = new PlayerPrimaryKey( childId );
			
			try
			{
				// find the Player
				child = childDelegate.getPlayer( pk );
				LOGGER.info( "LeagueRestService:saveLeague() - found Player" );
			}
			catch( Exception exc )
			{
	        	signalBadRequest();

				String errMsg = "LeagueRestService:savePlayers() failed get child Player using id " + childId  + "- " + exc.getMessage();
				LOGGER.severe( errMsg );
				throw new ProcessingException( errMsg );
			}
			
			// add it to the Players, check for null
			if ( league.getPlayers() != null )
				league.getPlayers().add( child );

			LOGGER.info( "LeagueRestService:saveLeague() - added Player to parent" );

		}
		else
		{
			// clear or create the Players
			if ( league.getPlayers() != null )
				league.getPlayers().clear();
			else
				league.setPlayers( new HashSet<Player>() );
			
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
			        	signalBadRequest();

						String errMsg = "LeagueRestService:savePlayers() failed get child Player using id " + id  + "- " + exc.getMessage();
						LOGGER.severe( errMsg );
						throw new ProcessingException( errMsg );
					}
				}
			}
		}

		try
		{
			// save the League
			parentDelegate.saveLeague( league );
			LOGGER.info( "LeagueRestService:saveLeague() - saved successfully" );

		}
		catch( Exception exc )
		{
        	signalBadRequest();

			String errMsg = "LeagueRestService:savePlayers() failed saving parent League - " + exc.getMessage();
			LOGGER.severe( errMsg );
			throw new ProcessingException( errMsg );
		}

		return league;
	}

    /**
     * delete Players on League
     * @return		League
     * @exception	ProcessingException
     */     	
	protected League deletePlayers()
		throws ProcessingException
	{		
		Long leagueId = parseId( "leagueId" );
		Long childId = parseId( "childId" );
		
		if ( loadHelper( new LeaguePrimaryKey( leagueId ) ) == null )
			throw new ProcessingException( "League.deletePlayers() - failed to load using Id " + leagueId );

		Long[] childIds = getChildIds();
		
		if ( childIds.length > 0 )
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
					signalBadRequest();
					String errMsg = "LeagueRestService:deletePlayers() failed - " + exc.getMessage();
					LOGGER.severe( errMsg );
					throw new ProcessingException( errMsg );
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
				signalBadRequest();
				
				String errMsg = "LeagueRestService:deletePlayers() failed to save the League - " + exc.getMessage();				
				LOGGER.severe( errMsg );
				throw new ProcessingException( errMsg );
			}
		}
		
		return league;
	}



    protected League loadHelper( LeaguePrimaryKey pk )
    		throws ProcessingException
    {
    	try
        {
    		LOGGER.info( "League.loadHelper primary key is " + pk);
    		
        	if ( pk != null )
        	{
        		// load the contained instance of League
	            this.league = LeagueBusinessDelegate.getLeagueInstance().getLeague( pk );
	            
	            LOGGER.info( "LeagueRestService:loadHelper() - successfully loaded - " + this.league.toString() );             
			}
			else
			{
	        	signalBadRequest();

				String errMsg = "LeagueRestService:loadHelper() - null primary key provided.";				
				LOGGER.severe( errMsg );
	            throw new ProcessingException( errMsg );
			}	            
        }
        catch( Throwable exc )
        {
        	signalBadRequest();

        	String errMsg = "LeagueRestService:load() - failed to load League using pk " + pk + ", " + exc.getMessage();				
			LOGGER.severe( errMsg );
            throw new ProcessingException( errMsg );
        }

        return league;

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
	        case "savePlayers":
	        	returnVal = savePlayers().getPlayers();
	        	break;
	        case "deletePlayers":
	        	returnVal = deletePlayers().getPlayers();
	        	break;
	        case "loadPlayers" :
	        	returnVal = load().getPlayers();
	        	break;
          default:
        	signalBadRequest();
            throw new ProcessingException("League.execute(...) - unable to handle action " + action);
		}
		
		return returnVal;
	}
	
	/**
	 * Uses ObjectMapper to map from Json to a League. Found in the request body.
	 * 
	 * @return League
	 */
	private League getLeague()
	{
		if ( league == null )
		{
			try {
				ObjectMapper mapper = new ObjectMapper();
				mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
				league = mapper.readValue(java.net.URLDecoder.decode(request.queryString(),"UTF-8"), League.class);
				
	        } catch (Exception exc) 
	        {
	            signalBadRequest();
	            LOGGER.severe( "LeagueRestService.getLeague() - failed to Json map from String to League - " + exc.getMessage() );
	        }			
		}
		return( league );
	}
	
	protected String getSubclassName()
	{ return( "LeagueRestService" ); }
	
//************************************************************************    
// Attributes
//************************************************************************
    private League league 			= null;
    private static final Logger LOGGER 	= Logger.getLogger(BaseRestService.class.getName());
    
}
