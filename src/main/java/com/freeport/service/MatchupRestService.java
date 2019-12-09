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
 * Implements Struts action processing for business entity Matchup.
 *
 * @author Dev Team
 */

public class MatchupRestService extends BaseRestService
{

	public MatchupRestService()
	{}
	
    /**
     * Handles saving a Matchup BO.  if not key provided, calls create, otherwise calls save
     * @exception	ProcessingException
     */
    protected Matchup save()
    	throws ProcessingException
    {
		// doing it here helps
		getMatchup();

		LOGGER.info( "Matchup.save() on - " + matchup );
		
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
     * Returns true if the matchup is non-null and has it's primary key field(s) set
     * @return		boolean
     */
    protected boolean hasPrimaryKey()
    {
    	boolean hasPK = false;

		if ( matchup != null && matchup.getMatchupPrimaryKey().hasBeenAssigned() == true )
		   hasPK = true;
		
		return( hasPK );
    }

    /**
     * Handles updating a Matchup BO
     * @return		Matchup
     * @exception	ProcessingException
     */    
    protected Matchup update()
    	throws ProcessingException
    {
    	// store provided data
        Matchup tmp = matchup;

        // load actual data from storage
        loadHelper( matchup.getMatchupPrimaryKey() );
    	
    	// copy provided data into actual data
    	matchup.copyShallow( tmp );
    	
        try
        {                        	        
			// create the MatchupBusiness Delegate            
			MatchupBusinessDelegate delegate = MatchupBusinessDelegate.getMatchupInstance();
            this.matchup = delegate.saveMatchup( matchup );
            
            if ( this.matchup != null )
            	LOGGER.info( "MatchupRestService:update() - successfully updated Matchup - " + matchup.toString() );
        }
        catch( Throwable exc )
        {
        	signalBadRequest();
        	
        	String errMsg = "MatchupRestService:update() - successfully update Matchup - " + exc.getMessage();
        	LOGGER.severe( errMsg );
        	throw new ProcessingException( errMsg );
        }
        
        return this.matchup;
        
    }

    /**
     * Handles creating a Matchup BO
     * @return		Matchup
     */
    protected Matchup create()
    	throws ProcessingException
    {
        try
        {       
        	matchup 		= getMatchup();
			this.matchup 	= MatchupBusinessDelegate.getMatchupInstance().createMatchup( matchup );
        }
        catch( Throwable exc )
        {
        	signalBadRequest();
        	
        	String errMsg = "MatchupRestService:create() - exception Matchup - " + exc.getMessage();
        	LOGGER.severe( errMsg );
        	throw new ProcessingException( errMsg );
        }
        
        return this.matchup;
    }


    
    /**
     * Handles deleting a Matchup BO
     * @exception	ProcessingException
     */
    protected void delete() 
    	throws ProcessingException
    {                
        try
        {
        	MatchupBusinessDelegate delegate = MatchupBusinessDelegate.getMatchupInstance();

        	Long[] childIds = getChildIds();
        	
        	if ( childIds == null || childIds.length == 0 )
        	{
        		Long matchupId  = parseId( "matchupId" );
        		delegate.delete( new MatchupPrimaryKey( matchupId  ) );
        		LOGGER.info( "MatchupRestService:delete() - successfully deleted Matchup with key " + matchup.getMatchupPrimaryKey().valuesAsCollection() );
        	}
        	else
        	{
        		for ( Long id : childIds )
        		{
        			try
        			{
        				delegate.delete( new MatchupPrimaryKey( id ) );
        			}
	                catch( Throwable exc )
	                {
	                	signalBadRequest();

	                	String errMsg = "MatchupRestService:delete() - " + exc.getMessage();
	                	LOGGER.severe( errMsg );
	                	throw new ProcessingException( errMsg );
	                }
        		}
        	}
        }
        catch( Throwable exc )
        {
        	signalBadRequest();
        	String errMsg = "MatchupRestService:delete() - " + exc.getMessage();
        	LOGGER.severe( errMsg );
        	throw new ProcessingException( errMsg );
        }
	}        
	
    /**
     * Handles loading a Matchup BO
     * @param		Long matchupId
     * @exception	ProcessingException
     * @return		Matchup
     */    
    protected Matchup load() 
    	throws ProcessingException
    {    	
        MatchupPrimaryKey pk 	= null;
		Long matchupId  = parseId( "matchupId" );

    	try
        {
    		LOGGER.info( "Matchup.load pk is " + matchupId );
    		
        	if ( matchupId != null )
        	{
        		pk = new MatchupPrimaryKey( matchupId );

        		loadHelper( pk );

        		// load the contained instance of Matchup
	            this.matchup = MatchupBusinessDelegate.getMatchupInstance().getMatchup( pk );
	            
	            LOGGER.info( "MatchupRestService:load() - successfully loaded - " + this.matchup.toString() );             
			}
			else
			{
	        	signalBadRequest();

				String errMsg = "MatchupRestService:load() - unable to locate the primary key as an attribute or a selection for - " + matchup.toString();				
				LOGGER.severe( errMsg );
	            throw new ProcessingException( errMsg );
			}	            
        }
        catch( Throwable exc )
        {
        	signalBadRequest();

        	String errMsg = "MatchupRestService:load() - failed to load Matchup using Id " + matchupId + ", " + exc.getMessage();				
			LOGGER.severe( errMsg );
            throw new ProcessingException( errMsg );
        }

        return matchup;

    }

    /**
     * Handles loading all Matchup business objects
     * @return		List<Matchup>
     * @exception	ProcessingException
     */
    protected List<Matchup> loadAll()
    	throws ProcessingException
    {                
        List<Matchup> matchupList = null;
        
    	try
        {                        
            // load the Matchup
            matchupList = MatchupBusinessDelegate.getMatchupInstance().getAllMatchup();
            
            if ( matchupList != null )
            	LOGGER.info(  "MatchupRestService:loadAllMatchup() - successfully loaded all Matchups" );
        }
        catch( Throwable exc )
        {
        	signalBadRequest();

        	String errMsg = "MatchupRestService:loadAll() - failed to load all Matchups - " + exc.getMessage();				
			LOGGER.severe( errMsg );
            throw new ProcessingException( errMsg );            
        }

        return matchupList;
                            
    }




    /**
     * save Games on Matchup
     * @param		Long matchupId
     * @param		Long childId
     * @param		String[] childIds
     * @return		Matchup
     * @exception	ProcessingException
     */     
	protected Matchup saveGames()
		throws ProcessingException
	{
		Long matchupId = parseId( "matchupId" );
		Long childId = parseId( "childId" );
		
		if ( loadHelper( new MatchupPrimaryKey( matchupId ) ) == null )
			throw new ProcessingException( "Matchup.saveGames() - failed to load parent using Id " + matchupId );
		 
		GamePrimaryKey pk 					= null;
		Game child							= null;
		List<Game> childList				= null;
		GameBusinessDelegate childDelegate 	= GameBusinessDelegate.getGameInstance();
		MatchupBusinessDelegate parentDelegate = MatchupBusinessDelegate.getMatchupInstance();		
		
		Long[] childIds = getChildIds();
		
		if ( childId != null || childIds.length == 0 )// creating or saving one
		{
			pk = new GamePrimaryKey( childId );
			
			try
			{
				// find the Game
				child = childDelegate.getGame( pk );
				LOGGER.info( "LeagueRestService:saveMatchup() - found Game" );
			}
			catch( Exception exc )
			{
	        	signalBadRequest();

				String errMsg = "MatchupRestService:saveGames() failed get child Game using id " + childId  + "- " + exc.getMessage();
				LOGGER.severe( errMsg );
				throw new ProcessingException( errMsg );
			}
			
			// add it to the Games, check for null
			if ( matchup.getGames() != null )
				matchup.getGames().add( child );

			LOGGER.info( "LeagueRestService:saveMatchup() - added Game to parent" );

		}
		else
		{
			// clear or create the Games
			if ( matchup.getGames() != null )
				matchup.getGames().clear();
			else
				matchup.setGames( new HashSet<Game>() );
			
			// finally, find each child and add it
			if ( childIds != null )
			{
				for( Long id : childIds )
				{
					pk = new GamePrimaryKey( id );
					try
					{
						// find the Game
						child = childDelegate.getGame( pk );
						// add it to the Games List
						matchup.getGames().add( child );
					}
					catch( Exception exc )
					{
			        	signalBadRequest();

						String errMsg = "MatchupRestService:saveGames() failed get child Game using id " + id  + "- " + exc.getMessage();
						LOGGER.severe( errMsg );
						throw new ProcessingException( errMsg );
					}
				}
			}
		}

		try
		{
			// save the Matchup
			parentDelegate.saveMatchup( matchup );
			LOGGER.info( "LeagueRestService:saveMatchup() - saved successfully" );

		}
		catch( Exception exc )
		{
        	signalBadRequest();

			String errMsg = "MatchupRestService:saveGames() failed saving parent Matchup - " + exc.getMessage();
			LOGGER.severe( errMsg );
			throw new ProcessingException( errMsg );
		}

		return matchup;
	}

    /**
     * delete Games on Matchup
     * @return		Matchup
     * @exception	ProcessingException
     */     	
	protected Matchup deleteGames()
		throws ProcessingException
	{		
		Long matchupId = parseId( "matchupId" );
		Long childId = parseId( "childId" );
		
		if ( loadHelper( new MatchupPrimaryKey( matchupId ) ) == null )
			throw new ProcessingException( "Matchup.deleteGames() - failed to load using Id " + matchupId );

		Long[] childIds = getChildIds();
		
		if ( childIds.length > 0 )
		{
			GamePrimaryKey pk 					= null;
			GameBusinessDelegate childDelegate 	= GameBusinessDelegate.getGameInstance();
			MatchupBusinessDelegate parentDelegate = MatchupBusinessDelegate.getMatchupInstance();
			Set<Game> children					= matchup.getGames();
			Game child 							= null;
			
			for( Long id : childIds )
			{
				try
				{
					pk = new GamePrimaryKey( id );
					
					// first remove the relevant child from the list
					child = childDelegate.getGame( pk );
					children.remove( child );
					
					// then safe to delete the child				
					childDelegate.delete( pk );
				}
				catch( Exception exc )
				{
					signalBadRequest();
					String errMsg = "MatchupRestService:deleteGames() failed - " + exc.getMessage();
					LOGGER.severe( errMsg );
					throw new ProcessingException( errMsg );
				}
			}
			
			// assign the modified list of Game back to the matchup
			matchup.setGames( children );
			
			// save it 
			try
			{
				matchup = parentDelegate.saveMatchup( matchup );
			}
			catch( Throwable exc )
			{
				signalBadRequest();
				
				String errMsg = "MatchupRestService:deleteGames() failed to save the Matchup - " + exc.getMessage();				
				LOGGER.severe( errMsg );
				throw new ProcessingException( errMsg );
			}
		}
		
		return matchup;
	}



    protected Matchup loadHelper( MatchupPrimaryKey pk )
    		throws ProcessingException
    {
    	try
        {
    		LOGGER.info( "Matchup.loadHelper primary key is " + pk);
    		
        	if ( pk != null )
        	{
        		// load the contained instance of Matchup
	            this.matchup = MatchupBusinessDelegate.getMatchupInstance().getMatchup( pk );
	            
	            LOGGER.info( "MatchupRestService:loadHelper() - successfully loaded - " + this.matchup.toString() );             
			}
			else
			{
	        	signalBadRequest();

				String errMsg = "MatchupRestService:loadHelper() - null primary key provided.";				
				LOGGER.severe( errMsg );
	            throw new ProcessingException( errMsg );
			}	            
        }
        catch( Throwable exc )
        {
        	signalBadRequest();

        	String errMsg = "MatchupRestService:load() - failed to load Matchup using pk " + pk + ", " + exc.getMessage();				
			LOGGER.severe( errMsg );
            throw new ProcessingException( errMsg );
        }

        return matchup;

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
	        case "saveGames":
	        	returnVal = saveGames().getGames();
	        	break;
	        case "deleteGames":
	        	returnVal = deleteGames().getGames();
	        	break;
	        case "loadGames" :
	        	returnVal = load().getGames();
	        	break;
          default:
        	signalBadRequest();
            throw new ProcessingException("Matchup.execute(...) - unable to handle action " + action);
		}
		
		return returnVal;
	}
	
	/**
	 * Uses ObjectMapper to map from Json to a Matchup. Found in the request body.
	 * 
	 * @return Matchup
	 */
	private Matchup getMatchup()
	{
		if ( matchup == null )
		{
			try {
				ObjectMapper mapper = new ObjectMapper();
				mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
				matchup = mapper.readValue(java.net.URLDecoder.decode(request.queryString(),"UTF-8"), Matchup.class);
				
	        } catch (Exception exc) 
	        {
	            signalBadRequest();
	            LOGGER.severe( "MatchupRestService.getMatchup() - failed to Json map from String to Matchup - " + exc.getMessage() );
	        }			
		}
		return( matchup );
	}
	
	protected String getSubclassName()
	{ return( "MatchupRestService" ); }
	
//************************************************************************    
// Attributes
//************************************************************************
    private Matchup matchup 			= null;
    private static final Logger LOGGER 	= Logger.getLogger(BaseRestService.class.getName());
    
}
