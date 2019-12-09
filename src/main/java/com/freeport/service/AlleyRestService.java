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
 * Implements Struts action processing for business entity Alley.
 *
 * @author Dev Team
 */

public class AlleyRestService extends BaseRestService
{

	public AlleyRestService()
	{}
	
    /**
     * Handles saving a Alley BO.  if not key provided, calls create, otherwise calls save
     * @exception	ProcessingException
     */
    protected Alley save()
    	throws ProcessingException
    {
		// doing it here helps
		getAlley();

		LOGGER.info( "Alley.save() on - " + alley );
		
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
     * Returns true if the alley is non-null and has it's primary key field(s) set
     * @return		boolean
     */
    protected boolean hasPrimaryKey()
    {
    	boolean hasPK = false;

		if ( alley != null && alley.getAlleyPrimaryKey().hasBeenAssigned() == true )
		   hasPK = true;
		
		return( hasPK );
    }

    /**
     * Handles updating a Alley BO
     * @return		Alley
     * @exception	ProcessingException
     */    
    protected Alley update()
    	throws ProcessingException
    {
    	// store provided data
        Alley tmp = alley;

        // load actual data from storage
        loadHelper( alley.getAlleyPrimaryKey() );
    	
    	// copy provided data into actual data
    	alley.copyShallow( tmp );
    	
        try
        {                        	        
			// create the AlleyBusiness Delegate            
			AlleyBusinessDelegate delegate = AlleyBusinessDelegate.getAlleyInstance();
            this.alley = delegate.saveAlley( alley );
            
            if ( this.alley != null )
            	LOGGER.info( "AlleyRestService:update() - successfully updated Alley - " + alley.toString() );
        }
        catch( Throwable exc )
        {
        	signalBadRequest();
        	
        	String errMsg = "AlleyRestService:update() - successfully update Alley - " + exc.getMessage();
        	LOGGER.severe( errMsg );
        	throw new ProcessingException( errMsg );
        }
        
        return this.alley;
        
    }

    /**
     * Handles creating a Alley BO
     * @return		Alley
     */
    protected Alley create()
    	throws ProcessingException
    {
        try
        {       
        	alley 		= getAlley();
			this.alley 	= AlleyBusinessDelegate.getAlleyInstance().createAlley( alley );
        }
        catch( Throwable exc )
        {
        	signalBadRequest();
        	
        	String errMsg = "AlleyRestService:create() - exception Alley - " + exc.getMessage();
        	LOGGER.severe( errMsg );
        	throw new ProcessingException( errMsg );
        }
        
        return this.alley;
    }


    
    /**
     * Handles deleting a Alley BO
     * @exception	ProcessingException
     */
    protected void delete() 
    	throws ProcessingException
    {                
        try
        {
        	AlleyBusinessDelegate delegate = AlleyBusinessDelegate.getAlleyInstance();

        	Long[] childIds = getChildIds();
        	
        	if ( childIds == null || childIds.length == 0 )
        	{
        		Long alleyId  = parseId( "alleyId" );
        		delegate.delete( new AlleyPrimaryKey( alleyId  ) );
        		LOGGER.info( "AlleyRestService:delete() - successfully deleted Alley with key " + alley.getAlleyPrimaryKey().valuesAsCollection() );
        	}
        	else
        	{
        		for ( Long id : childIds )
        		{
        			try
        			{
        				delegate.delete( new AlleyPrimaryKey( id ) );
        			}
	                catch( Throwable exc )
	                {
	                	signalBadRequest();

	                	String errMsg = "AlleyRestService:delete() - " + exc.getMessage();
	                	LOGGER.severe( errMsg );
	                	throw new ProcessingException( errMsg );
	                }
        		}
        	}
        }
        catch( Throwable exc )
        {
        	signalBadRequest();
        	String errMsg = "AlleyRestService:delete() - " + exc.getMessage();
        	LOGGER.severe( errMsg );
        	throw new ProcessingException( errMsg );
        }
	}        
	
    /**
     * Handles loading a Alley BO
     * @param		Long alleyId
     * @exception	ProcessingException
     * @return		Alley
     */    
    protected Alley load() 
    	throws ProcessingException
    {    	
        AlleyPrimaryKey pk 	= null;
		Long alleyId  = parseId( "alleyId" );

    	try
        {
    		LOGGER.info( "Alley.load pk is " + alleyId );
    		
        	if ( alleyId != null )
        	{
        		pk = new AlleyPrimaryKey( alleyId );

        		loadHelper( pk );

        		// load the contained instance of Alley
	            this.alley = AlleyBusinessDelegate.getAlleyInstance().getAlley( pk );
	            
	            LOGGER.info( "AlleyRestService:load() - successfully loaded - " + this.alley.toString() );             
			}
			else
			{
	        	signalBadRequest();

				String errMsg = "AlleyRestService:load() - unable to locate the primary key as an attribute or a selection for - " + alley.toString();				
				LOGGER.severe( errMsg );
	            throw new ProcessingException( errMsg );
			}	            
        }
        catch( Throwable exc )
        {
        	signalBadRequest();

        	String errMsg = "AlleyRestService:load() - failed to load Alley using Id " + alleyId + ", " + exc.getMessage();				
			LOGGER.severe( errMsg );
            throw new ProcessingException( errMsg );
        }

        return alley;

    }

    /**
     * Handles loading all Alley business objects
     * @return		List<Alley>
     * @exception	ProcessingException
     */
    protected List<Alley> loadAll()
    	throws ProcessingException
    {                
        List<Alley> alleyList = null;
        
    	try
        {                        
            // load the Alley
            alleyList = AlleyBusinessDelegate.getAlleyInstance().getAllAlley();
            
            if ( alleyList != null )
            	LOGGER.info(  "AlleyRestService:loadAllAlley() - successfully loaded all Alleys" );
        }
        catch( Throwable exc )
        {
        	signalBadRequest();

        	String errMsg = "AlleyRestService:loadAll() - failed to load all Alleys - " + exc.getMessage();				
			LOGGER.severe( errMsg );
            throw new ProcessingException( errMsg );            
        }

        return alleyList;
                            
    }




    /**
     * save Leagues on Alley
     * @param		Long alleyId
     * @param		Long childId
     * @param		String[] childIds
     * @return		Alley
     * @exception	ProcessingException
     */     
	protected Alley saveLeagues()
		throws ProcessingException
	{
		Long alleyId = parseId( "alleyId" );
		Long childId = parseId( "childId" );
		
		if ( loadHelper( new AlleyPrimaryKey( alleyId ) ) == null )
			throw new ProcessingException( "Alley.saveLeagues() - failed to load parent using Id " + alleyId );
		 
		LeaguePrimaryKey pk 					= null;
		League child							= null;
		List<League> childList				= null;
		LeagueBusinessDelegate childDelegate 	= LeagueBusinessDelegate.getLeagueInstance();
		AlleyBusinessDelegate parentDelegate = AlleyBusinessDelegate.getAlleyInstance();		
		
		Long[] childIds = getChildIds();
		
		if ( childId != null || childIds.length == 0 )// creating or saving one
		{
			pk = new LeaguePrimaryKey( childId );
			
			try
			{
				// find the League
				child = childDelegate.getLeague( pk );
				LOGGER.info( "LeagueRestService:saveAlley() - found League" );
			}
			catch( Exception exc )
			{
	        	signalBadRequest();

				String errMsg = "AlleyRestService:saveLeagues() failed get child League using id " + childId  + "- " + exc.getMessage();
				LOGGER.severe( errMsg );
				throw new ProcessingException( errMsg );
			}
			
			// add it to the Leagues, check for null
			if ( alley.getLeagues() != null )
				alley.getLeagues().add( child );

			LOGGER.info( "LeagueRestService:saveAlley() - added League to parent" );

		}
		else
		{
			// clear or create the Leagues
			if ( alley.getLeagues() != null )
				alley.getLeagues().clear();
			else
				alley.setLeagues( new HashSet<League>() );
			
			// finally, find each child and add it
			if ( childIds != null )
			{
				for( Long id : childIds )
				{
					pk = new LeaguePrimaryKey( id );
					try
					{
						// find the League
						child = childDelegate.getLeague( pk );
						// add it to the Leagues List
						alley.getLeagues().add( child );
					}
					catch( Exception exc )
					{
			        	signalBadRequest();

						String errMsg = "AlleyRestService:saveLeagues() failed get child League using id " + id  + "- " + exc.getMessage();
						LOGGER.severe( errMsg );
						throw new ProcessingException( errMsg );
					}
				}
			}
		}

		try
		{
			// save the Alley
			parentDelegate.saveAlley( alley );
			LOGGER.info( "LeagueRestService:saveAlley() - saved successfully" );

		}
		catch( Exception exc )
		{
        	signalBadRequest();

			String errMsg = "AlleyRestService:saveLeagues() failed saving parent Alley - " + exc.getMessage();
			LOGGER.severe( errMsg );
			throw new ProcessingException( errMsg );
		}

		return alley;
	}

    /**
     * delete Leagues on Alley
     * @return		Alley
     * @exception	ProcessingException
     */     	
	protected Alley deleteLeagues()
		throws ProcessingException
	{		
		Long alleyId = parseId( "alleyId" );
		Long childId = parseId( "childId" );
		
		if ( loadHelper( new AlleyPrimaryKey( alleyId ) ) == null )
			throw new ProcessingException( "Alley.deleteLeagues() - failed to load using Id " + alleyId );

		Long[] childIds = getChildIds();
		
		if ( childIds.length > 0 )
		{
			LeaguePrimaryKey pk 					= null;
			LeagueBusinessDelegate childDelegate 	= LeagueBusinessDelegate.getLeagueInstance();
			AlleyBusinessDelegate parentDelegate = AlleyBusinessDelegate.getAlleyInstance();
			Set<League> children					= alley.getLeagues();
			League child 							= null;
			
			for( Long id : childIds )
			{
				try
				{
					pk = new LeaguePrimaryKey( id );
					
					// first remove the relevant child from the list
					child = childDelegate.getLeague( pk );
					children.remove( child );
					
					// then safe to delete the child				
					childDelegate.delete( pk );
				}
				catch( Exception exc )
				{
					signalBadRequest();
					String errMsg = "AlleyRestService:deleteLeagues() failed - " + exc.getMessage();
					LOGGER.severe( errMsg );
					throw new ProcessingException( errMsg );
				}
			}
			
			// assign the modified list of League back to the alley
			alley.setLeagues( children );
			
			// save it 
			try
			{
				alley = parentDelegate.saveAlley( alley );
			}
			catch( Throwable exc )
			{
				signalBadRequest();
				
				String errMsg = "AlleyRestService:deleteLeagues() failed to save the Alley - " + exc.getMessage();				
				LOGGER.severe( errMsg );
				throw new ProcessingException( errMsg );
			}
		}
		
		return alley;
	}

    /**
     * save Tournaments on Alley
     * @param		Long alleyId
     * @param		Long childId
     * @param		String[] childIds
     * @return		Alley
     * @exception	ProcessingException
     */     
	protected Alley saveTournaments()
		throws ProcessingException
	{
		Long alleyId = parseId( "alleyId" );
		Long childId = parseId( "childId" );
		
		if ( loadHelper( new AlleyPrimaryKey( alleyId ) ) == null )
			throw new ProcessingException( "Alley.saveTournaments() - failed to load parent using Id " + alleyId );
		 
		TournamentPrimaryKey pk 					= null;
		Tournament child							= null;
		List<Tournament> childList				= null;
		TournamentBusinessDelegate childDelegate 	= TournamentBusinessDelegate.getTournamentInstance();
		AlleyBusinessDelegate parentDelegate = AlleyBusinessDelegate.getAlleyInstance();		
		
		Long[] childIds = getChildIds();
		
		if ( childId != null || childIds.length == 0 )// creating or saving one
		{
			pk = new TournamentPrimaryKey( childId );
			
			try
			{
				// find the Tournament
				child = childDelegate.getTournament( pk );
				LOGGER.info( "LeagueRestService:saveAlley() - found Tournament" );
			}
			catch( Exception exc )
			{
	        	signalBadRequest();

				String errMsg = "AlleyRestService:saveTournaments() failed get child Tournament using id " + childId  + "- " + exc.getMessage();
				LOGGER.severe( errMsg );
				throw new ProcessingException( errMsg );
			}
			
			// add it to the Tournaments, check for null
			if ( alley.getTournaments() != null )
				alley.getTournaments().add( child );

			LOGGER.info( "LeagueRestService:saveAlley() - added Tournament to parent" );

		}
		else
		{
			// clear or create the Tournaments
			if ( alley.getTournaments() != null )
				alley.getTournaments().clear();
			else
				alley.setTournaments( new HashSet<Tournament>() );
			
			// finally, find each child and add it
			if ( childIds != null )
			{
				for( Long id : childIds )
				{
					pk = new TournamentPrimaryKey( id );
					try
					{
						// find the Tournament
						child = childDelegate.getTournament( pk );
						// add it to the Tournaments List
						alley.getTournaments().add( child );
					}
					catch( Exception exc )
					{
			        	signalBadRequest();

						String errMsg = "AlleyRestService:saveTournaments() failed get child Tournament using id " + id  + "- " + exc.getMessage();
						LOGGER.severe( errMsg );
						throw new ProcessingException( errMsg );
					}
				}
			}
		}

		try
		{
			// save the Alley
			parentDelegate.saveAlley( alley );
			LOGGER.info( "LeagueRestService:saveAlley() - saved successfully" );

		}
		catch( Exception exc )
		{
        	signalBadRequest();

			String errMsg = "AlleyRestService:saveTournaments() failed saving parent Alley - " + exc.getMessage();
			LOGGER.severe( errMsg );
			throw new ProcessingException( errMsg );
		}

		return alley;
	}

    /**
     * delete Tournaments on Alley
     * @return		Alley
     * @exception	ProcessingException
     */     	
	protected Alley deleteTournaments()
		throws ProcessingException
	{		
		Long alleyId = parseId( "alleyId" );
		Long childId = parseId( "childId" );
		
		if ( loadHelper( new AlleyPrimaryKey( alleyId ) ) == null )
			throw new ProcessingException( "Alley.deleteTournaments() - failed to load using Id " + alleyId );

		Long[] childIds = getChildIds();
		
		if ( childIds.length > 0 )
		{
			TournamentPrimaryKey pk 					= null;
			TournamentBusinessDelegate childDelegate 	= TournamentBusinessDelegate.getTournamentInstance();
			AlleyBusinessDelegate parentDelegate = AlleyBusinessDelegate.getAlleyInstance();
			Set<Tournament> children					= alley.getTournaments();
			Tournament child 							= null;
			
			for( Long id : childIds )
			{
				try
				{
					pk = new TournamentPrimaryKey( id );
					
					// first remove the relevant child from the list
					child = childDelegate.getTournament( pk );
					children.remove( child );
					
					// then safe to delete the child				
					childDelegate.delete( pk );
				}
				catch( Exception exc )
				{
					signalBadRequest();
					String errMsg = "AlleyRestService:deleteTournaments() failed - " + exc.getMessage();
					LOGGER.severe( errMsg );
					throw new ProcessingException( errMsg );
				}
			}
			
			// assign the modified list of Tournament back to the alley
			alley.setTournaments( children );
			
			// save it 
			try
			{
				alley = parentDelegate.saveAlley( alley );
			}
			catch( Throwable exc )
			{
				signalBadRequest();
				
				String errMsg = "AlleyRestService:deleteTournaments() failed to save the Alley - " + exc.getMessage();				
				LOGGER.severe( errMsg );
				throw new ProcessingException( errMsg );
			}
		}
		
		return alley;
	}

    /**
     * save Lanes on Alley
     * @param		Long alleyId
     * @param		Long childId
     * @param		String[] childIds
     * @return		Alley
     * @exception	ProcessingException
     */     
	protected Alley saveLanes()
		throws ProcessingException
	{
		Long alleyId = parseId( "alleyId" );
		Long childId = parseId( "childId" );
		
		if ( loadHelper( new AlleyPrimaryKey( alleyId ) ) == null )
			throw new ProcessingException( "Alley.saveLanes() - failed to load parent using Id " + alleyId );
		 
		LanePrimaryKey pk 					= null;
		Lane child							= null;
		List<Lane> childList				= null;
		LaneBusinessDelegate childDelegate 	= LaneBusinessDelegate.getLaneInstance();
		AlleyBusinessDelegate parentDelegate = AlleyBusinessDelegate.getAlleyInstance();		
		
		Long[] childIds = getChildIds();
		
		if ( childId != null || childIds.length == 0 )// creating or saving one
		{
			pk = new LanePrimaryKey( childId );
			
			try
			{
				// find the Lane
				child = childDelegate.getLane( pk );
				LOGGER.info( "LeagueRestService:saveAlley() - found Lane" );
			}
			catch( Exception exc )
			{
	        	signalBadRequest();

				String errMsg = "AlleyRestService:saveLanes() failed get child Lane using id " + childId  + "- " + exc.getMessage();
				LOGGER.severe( errMsg );
				throw new ProcessingException( errMsg );
			}
			
			// add it to the Lanes, check for null
			if ( alley.getLanes() != null )
				alley.getLanes().add( child );

			LOGGER.info( "LeagueRestService:saveAlley() - added Lane to parent" );

		}
		else
		{
			// clear or create the Lanes
			if ( alley.getLanes() != null )
				alley.getLanes().clear();
			else
				alley.setLanes( new HashSet<Lane>() );
			
			// finally, find each child and add it
			if ( childIds != null )
			{
				for( Long id : childIds )
				{
					pk = new LanePrimaryKey( id );
					try
					{
						// find the Lane
						child = childDelegate.getLane( pk );
						// add it to the Lanes List
						alley.getLanes().add( child );
					}
					catch( Exception exc )
					{
			        	signalBadRequest();

						String errMsg = "AlleyRestService:saveLanes() failed get child Lane using id " + id  + "- " + exc.getMessage();
						LOGGER.severe( errMsg );
						throw new ProcessingException( errMsg );
					}
				}
			}
		}

		try
		{
			// save the Alley
			parentDelegate.saveAlley( alley );
			LOGGER.info( "LeagueRestService:saveAlley() - saved successfully" );

		}
		catch( Exception exc )
		{
        	signalBadRequest();

			String errMsg = "AlleyRestService:saveLanes() failed saving parent Alley - " + exc.getMessage();
			LOGGER.severe( errMsg );
			throw new ProcessingException( errMsg );
		}

		return alley;
	}

    /**
     * delete Lanes on Alley
     * @return		Alley
     * @exception	ProcessingException
     */     	
	protected Alley deleteLanes()
		throws ProcessingException
	{		
		Long alleyId = parseId( "alleyId" );
		Long childId = parseId( "childId" );
		
		if ( loadHelper( new AlleyPrimaryKey( alleyId ) ) == null )
			throw new ProcessingException( "Alley.deleteLanes() - failed to load using Id " + alleyId );

		Long[] childIds = getChildIds();
		
		if ( childIds.length > 0 )
		{
			LanePrimaryKey pk 					= null;
			LaneBusinessDelegate childDelegate 	= LaneBusinessDelegate.getLaneInstance();
			AlleyBusinessDelegate parentDelegate = AlleyBusinessDelegate.getAlleyInstance();
			Set<Lane> children					= alley.getLanes();
			Lane child 							= null;
			
			for( Long id : childIds )
			{
				try
				{
					pk = new LanePrimaryKey( id );
					
					// first remove the relevant child from the list
					child = childDelegate.getLane( pk );
					children.remove( child );
					
					// then safe to delete the child				
					childDelegate.delete( pk );
				}
				catch( Exception exc )
				{
					signalBadRequest();
					String errMsg = "AlleyRestService:deleteLanes() failed - " + exc.getMessage();
					LOGGER.severe( errMsg );
					throw new ProcessingException( errMsg );
				}
			}
			
			// assign the modified list of Lane back to the alley
			alley.setLanes( children );
			
			// save it 
			try
			{
				alley = parentDelegate.saveAlley( alley );
			}
			catch( Throwable exc )
			{
				signalBadRequest();
				
				String errMsg = "AlleyRestService:deleteLanes() failed to save the Alley - " + exc.getMessage();				
				LOGGER.severe( errMsg );
				throw new ProcessingException( errMsg );
			}
		}
		
		return alley;
	}



    protected Alley loadHelper( AlleyPrimaryKey pk )
    		throws ProcessingException
    {
    	try
        {
    		LOGGER.info( "Alley.loadHelper primary key is " + pk);
    		
        	if ( pk != null )
        	{
        		// load the contained instance of Alley
	            this.alley = AlleyBusinessDelegate.getAlleyInstance().getAlley( pk );
	            
	            LOGGER.info( "AlleyRestService:loadHelper() - successfully loaded - " + this.alley.toString() );             
			}
			else
			{
	        	signalBadRequest();

				String errMsg = "AlleyRestService:loadHelper() - null primary key provided.";				
				LOGGER.severe( errMsg );
	            throw new ProcessingException( errMsg );
			}	            
        }
        catch( Throwable exc )
        {
        	signalBadRequest();

        	String errMsg = "AlleyRestService:load() - failed to load Alley using pk " + pk + ", " + exc.getMessage();				
			LOGGER.severe( errMsg );
            throw new ProcessingException( errMsg );
        }

        return alley;

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
	        case "saveLeagues":
	        	returnVal = saveLeagues().getLeagues();
	        	break;
	        case "deleteLeagues":
	        	returnVal = deleteLeagues().getLeagues();
	        	break;
	        case "loadLeagues" :
	        	returnVal = load().getLeagues();
	        	break;
	        case "saveTournaments":
	        	returnVal = saveTournaments().getTournaments();
	        	break;
	        case "deleteTournaments":
	        	returnVal = deleteTournaments().getTournaments();
	        	break;
	        case "loadTournaments" :
	        	returnVal = load().getTournaments();
	        	break;
	        case "saveLanes":
	        	returnVal = saveLanes().getLanes();
	        	break;
	        case "deleteLanes":
	        	returnVal = deleteLanes().getLanes();
	        	break;
	        case "loadLanes" :
	        	returnVal = load().getLanes();
	        	break;
          default:
        	signalBadRequest();
            throw new ProcessingException("Alley.execute(...) - unable to handle action " + action);
		}
		
		return returnVal;
	}
	
	/**
	 * Uses ObjectMapper to map from Json to a Alley. Found in the request body.
	 * 
	 * @return Alley
	 */
	private Alley getAlley()
	{
		if ( alley == null )
		{
			try {
				ObjectMapper mapper = new ObjectMapper();
				mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
				alley = mapper.readValue(java.net.URLDecoder.decode(request.queryString(),"UTF-8"), Alley.class);
				
	        } catch (Exception exc) 
	        {
	            signalBadRequest();
	            LOGGER.severe( "AlleyRestService.getAlley() - failed to Json map from String to Alley - " + exc.getMessage() );
	        }			
		}
		return( alley );
	}
	
	protected String getSubclassName()
	{ return( "AlleyRestService" ); }
	
//************************************************************************    
// Attributes
//************************************************************************
    private Alley alley 			= null;
    private static final Logger LOGGER 	= Logger.getLogger(BaseRestService.class.getName());
    
}
