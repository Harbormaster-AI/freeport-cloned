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
 * Implements Struts action processing for business entity Tournament.
 *
 * @author Dev Team
 */

public class TournamentRestService extends BaseRestService
{

	public TournamentRestService()
	{}
	
    /**
     * Handles saving a Tournament BO.  if not key provided, calls create, otherwise calls save
     * @exception	ProcessingException
     */
    protected Tournament save()
    	throws ProcessingException
    {
		// doing it here helps
		getTournament();

		LOGGER.info( "Tournament.save() on - " + tournament );
		
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
     * Returns true if the tournament is non-null and has it's primary key field(s) set
     * @return		boolean
     */
    protected boolean hasPrimaryKey()
    {
    	boolean hasPK = false;

		if ( tournament != null && tournament.getTournamentPrimaryKey().hasBeenAssigned() == true )
		   hasPK = true;
		
		return( hasPK );
    }

    /**
     * Handles updating a Tournament BO
     * @return		Tournament
     * @exception	ProcessingException
     */    
    protected Tournament update()
    	throws ProcessingException
    {
    	// store provided data
        Tournament tmp = tournament;

        // load actual data from storage
        loadHelper( tournament.getTournamentPrimaryKey() );
    	
    	// copy provided data into actual data
    	tournament.copyShallow( tmp );
    	
        try
        {                        	        
			// create the TournamentBusiness Delegate            
			TournamentBusinessDelegate delegate = TournamentBusinessDelegate.getTournamentInstance();
            this.tournament = delegate.saveTournament( tournament );
            
            if ( this.tournament != null )
            	LOGGER.info( "TournamentRestService:update() - successfully updated Tournament - " + tournament.toString() );
        }
        catch( Throwable exc )
        {
        	signalBadRequest();
        	
        	String errMsg = "TournamentRestService:update() - successfully update Tournament - " + exc.getMessage();
        	LOGGER.severe( errMsg );
        	throw new ProcessingException( errMsg );
        }
        
        return this.tournament;
        
    }

    /**
     * Handles creating a Tournament BO
     * @return		Tournament
     */
    protected Tournament create()
    	throws ProcessingException
    {
        try
        {       
        	tournament 		= getTournament();
			this.tournament 	= TournamentBusinessDelegate.getTournamentInstance().createTournament( tournament );
        }
        catch( Throwable exc )
        {
        	signalBadRequest();
        	
        	String errMsg = "TournamentRestService:create() - exception Tournament - " + exc.getMessage();
        	LOGGER.severe( errMsg );
        	throw new ProcessingException( errMsg );
        }
        
        return this.tournament;
    }


    
    /**
     * Handles deleting a Tournament BO
     * @exception	ProcessingException
     */
    protected void delete() 
    	throws ProcessingException
    {                
        try
        {
        	TournamentBusinessDelegate delegate = TournamentBusinessDelegate.getTournamentInstance();

        	Long[] childIds = getChildIds();
        	
        	if ( childIds == null || childIds.length == 0 )
        	{
        		Long tournamentId  = parseId( "tournamentId" );
        		delegate.delete( new TournamentPrimaryKey( tournamentId  ) );
        		LOGGER.info( "TournamentRestService:delete() - successfully deleted Tournament with key " + tournament.getTournamentPrimaryKey().valuesAsCollection() );
        	}
        	else
        	{
        		for ( Long id : childIds )
        		{
        			try
        			{
        				delegate.delete( new TournamentPrimaryKey( id ) );
        			}
	                catch( Throwable exc )
	                {
	                	signalBadRequest();

	                	String errMsg = "TournamentRestService:delete() - " + exc.getMessage();
	                	LOGGER.severe( errMsg );
	                	throw new ProcessingException( errMsg );
	                }
        		}
        	}
        }
        catch( Throwable exc )
        {
        	signalBadRequest();
        	String errMsg = "TournamentRestService:delete() - " + exc.getMessage();
        	LOGGER.severe( errMsg );
        	throw new ProcessingException( errMsg );
        }
	}        
	
    /**
     * Handles loading a Tournament BO
     * @param		Long tournamentId
     * @exception	ProcessingException
     * @return		Tournament
     */    
    protected Tournament load() 
    	throws ProcessingException
    {    	
        TournamentPrimaryKey pk 	= null;
		Long tournamentId  = parseId( "tournamentId" );

    	try
        {
    		LOGGER.info( "Tournament.load pk is " + tournamentId );
    		
        	if ( tournamentId != null )
        	{
        		pk = new TournamentPrimaryKey( tournamentId );

        		loadHelper( pk );

        		// load the contained instance of Tournament
	            this.tournament = TournamentBusinessDelegate.getTournamentInstance().getTournament( pk );
	            
	            LOGGER.info( "TournamentRestService:load() - successfully loaded - " + this.tournament.toString() );             
			}
			else
			{
	        	signalBadRequest();

				String errMsg = "TournamentRestService:load() - unable to locate the primary key as an attribute or a selection for - " + tournament.toString();				
				LOGGER.severe( errMsg );
	            throw new ProcessingException( errMsg );
			}	            
        }
        catch( Throwable exc )
        {
        	signalBadRequest();

        	String errMsg = "TournamentRestService:load() - failed to load Tournament using Id " + tournamentId + ", " + exc.getMessage();				
			LOGGER.severe( errMsg );
            throw new ProcessingException( errMsg );
        }

        return tournament;

    }

    /**
     * Handles loading all Tournament business objects
     * @return		List<Tournament>
     * @exception	ProcessingException
     */
    protected List<Tournament> loadAll()
    	throws ProcessingException
    {                
        List<Tournament> tournamentList = null;
        
    	try
        {                        
            // load the Tournament
            tournamentList = TournamentBusinessDelegate.getTournamentInstance().getAllTournament();
            
            if ( tournamentList != null )
            	LOGGER.info(  "TournamentRestService:loadAllTournament() - successfully loaded all Tournaments" );
        }
        catch( Throwable exc )
        {
        	signalBadRequest();

        	String errMsg = "TournamentRestService:loadAll() - failed to load all Tournaments - " + exc.getMessage();				
			LOGGER.severe( errMsg );
            throw new ProcessingException( errMsg );            
        }

        return tournamentList;
                            
    }




    /**
     * save Matchups on Tournament
     * @param		Long tournamentId
     * @param		Long childId
     * @param		String[] childIds
     * @return		Tournament
     * @exception	ProcessingException
     */     
	protected Tournament saveMatchups()
		throws ProcessingException
	{
		Long tournamentId = parseId( "tournamentId" );
		Long childId = parseId( "childId" );
		
		if ( loadHelper( new TournamentPrimaryKey( tournamentId ) ) == null )
			throw new ProcessingException( "Tournament.saveMatchups() - failed to load parent using Id " + tournamentId );
		 
		MatchupPrimaryKey pk 					= null;
		Matchup child							= null;
		List<Matchup> childList				= null;
		MatchupBusinessDelegate childDelegate 	= MatchupBusinessDelegate.getMatchupInstance();
		TournamentBusinessDelegate parentDelegate = TournamentBusinessDelegate.getTournamentInstance();		
		
		Long[] childIds = getChildIds();
		
		if ( childId != null || childIds.length == 0 )// creating or saving one
		{
			pk = new MatchupPrimaryKey( childId );
			
			try
			{
				// find the Matchup
				child = childDelegate.getMatchup( pk );
				LOGGER.info( "LeagueRestService:saveTournament() - found Matchup" );
			}
			catch( Exception exc )
			{
	        	signalBadRequest();

				String errMsg = "TournamentRestService:saveMatchups() failed get child Matchup using id " + childId  + "- " + exc.getMessage();
				LOGGER.severe( errMsg );
				throw new ProcessingException( errMsg );
			}
			
			// add it to the Matchups, check for null
			if ( tournament.getMatchups() != null )
				tournament.getMatchups().add( child );

			LOGGER.info( "LeagueRestService:saveTournament() - added Matchup to parent" );

		}
		else
		{
			// clear or create the Matchups
			if ( tournament.getMatchups() != null )
				tournament.getMatchups().clear();
			else
				tournament.setMatchups( new HashSet<Matchup>() );
			
			// finally, find each child and add it
			if ( childIds != null )
			{
				for( Long id : childIds )
				{
					pk = new MatchupPrimaryKey( id );
					try
					{
						// find the Matchup
						child = childDelegate.getMatchup( pk );
						// add it to the Matchups List
						tournament.getMatchups().add( child );
					}
					catch( Exception exc )
					{
			        	signalBadRequest();

						String errMsg = "TournamentRestService:saveMatchups() failed get child Matchup using id " + id  + "- " + exc.getMessage();
						LOGGER.severe( errMsg );
						throw new ProcessingException( errMsg );
					}
				}
			}
		}

		try
		{
			// save the Tournament
			parentDelegate.saveTournament( tournament );
			LOGGER.info( "LeagueRestService:saveTournament() - saved successfully" );

		}
		catch( Exception exc )
		{
        	signalBadRequest();

			String errMsg = "TournamentRestService:saveMatchups() failed saving parent Tournament - " + exc.getMessage();
			LOGGER.severe( errMsg );
			throw new ProcessingException( errMsg );
		}

		return tournament;
	}

    /**
     * delete Matchups on Tournament
     * @return		Tournament
     * @exception	ProcessingException
     */     	
	protected Tournament deleteMatchups()
		throws ProcessingException
	{		
		Long tournamentId = parseId( "tournamentId" );
		Long childId = parseId( "childId" );
		
		if ( loadHelper( new TournamentPrimaryKey( tournamentId ) ) == null )
			throw new ProcessingException( "Tournament.deleteMatchups() - failed to load using Id " + tournamentId );

		Long[] childIds = getChildIds();
		
		if ( childIds.length > 0 )
		{
			MatchupPrimaryKey pk 					= null;
			MatchupBusinessDelegate childDelegate 	= MatchupBusinessDelegate.getMatchupInstance();
			TournamentBusinessDelegate parentDelegate = TournamentBusinessDelegate.getTournamentInstance();
			Set<Matchup> children					= tournament.getMatchups();
			Matchup child 							= null;
			
			for( Long id : childIds )
			{
				try
				{
					pk = new MatchupPrimaryKey( id );
					
					// first remove the relevant child from the list
					child = childDelegate.getMatchup( pk );
					children.remove( child );
					
					// then safe to delete the child				
					childDelegate.delete( pk );
				}
				catch( Exception exc )
				{
					signalBadRequest();
					String errMsg = "TournamentRestService:deleteMatchups() failed - " + exc.getMessage();
					LOGGER.severe( errMsg );
					throw new ProcessingException( errMsg );
				}
			}
			
			// assign the modified list of Matchup back to the tournament
			tournament.setMatchups( children );
			
			// save it 
			try
			{
				tournament = parentDelegate.saveTournament( tournament );
			}
			catch( Throwable exc )
			{
				signalBadRequest();
				
				String errMsg = "TournamentRestService:deleteMatchups() failed to save the Tournament - " + exc.getMessage();				
				LOGGER.severe( errMsg );
				throw new ProcessingException( errMsg );
			}
		}
		
		return tournament;
	}



    protected Tournament loadHelper( TournamentPrimaryKey pk )
    		throws ProcessingException
    {
    	try
        {
    		LOGGER.info( "Tournament.loadHelper primary key is " + pk);
    		
        	if ( pk != null )
        	{
        		// load the contained instance of Tournament
	            this.tournament = TournamentBusinessDelegate.getTournamentInstance().getTournament( pk );
	            
	            LOGGER.info( "TournamentRestService:loadHelper() - successfully loaded - " + this.tournament.toString() );             
			}
			else
			{
	        	signalBadRequest();

				String errMsg = "TournamentRestService:loadHelper() - null primary key provided.";				
				LOGGER.severe( errMsg );
	            throw new ProcessingException( errMsg );
			}	            
        }
        catch( Throwable exc )
        {
        	signalBadRequest();

        	String errMsg = "TournamentRestService:load() - failed to load Tournament using pk " + pk + ", " + exc.getMessage();				
			LOGGER.severe( errMsg );
            throw new ProcessingException( errMsg );
        }

        return tournament;

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
	        case "saveMatchups":
	        	returnVal = saveMatchups().getMatchups();
	        	break;
	        case "deleteMatchups":
	        	returnVal = deleteMatchups().getMatchups();
	        	break;
	        case "loadMatchups" :
	        	returnVal = load().getMatchups();
	        	break;
          default:
        	signalBadRequest();
            throw new ProcessingException("Tournament.execute(...) - unable to handle action " + action);
		}
		
		return returnVal;
	}
	
	/**
	 * Uses ObjectMapper to map from Json to a Tournament. Found in the request body.
	 * 
	 * @return Tournament
	 */
	private Tournament getTournament()
	{
		if ( tournament == null )
		{
			try {
				ObjectMapper mapper = new ObjectMapper();
				mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
				tournament = mapper.readValue(java.net.URLDecoder.decode(request.queryString(),"UTF-8"), Tournament.class);
				
	        } catch (Exception exc) 
	        {
	            signalBadRequest();
	            LOGGER.severe( "TournamentRestService.getTournament() - failed to Json map from String to Tournament - " + exc.getMessage() );
	        }			
		}
		return( tournament );
	}
	
	protected String getSubclassName()
	{ return( "TournamentRestService" ); }
	
//************************************************************************    
// Attributes
//************************************************************************
    private Tournament tournament 			= null;
    private static final Logger LOGGER 	= Logger.getLogger(BaseRestService.class.getName());
    
}
