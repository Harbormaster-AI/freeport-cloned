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
 * Implements Struts action processing for business entity Alley.
 *
 * @author Dev Team
 */
@RestController
@RequestMapping("/Alley")
public class AlleyRestController extends BaseSpringRestController
{

    /**
     * Handles saving a Alley BO.  if not key provided, calls create, otherwise calls save
     * @param		Alley alley
     * @return		Alley
     */
	@RequestMapping("/save")
    public Alley save( @RequestBody Alley alley )
    {
    	// assign locally
    	this.alley = alley;
    	
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
     * Handles deleting a Alley BO
     * @param		Long alleyId
     * @param 		Long[] childIds
     * @return		boolean
     */
    @RequestMapping("/delete")    
    public boolean delete( @RequestParam(value="alley.alleyId", required=false) Long alleyId, 
    						@RequestParam(value="childIds", required=false) Long[] childIds )
    {                
        try
        {
        	AlleyBusinessDelegate delegate = AlleyBusinessDelegate.getAlleyInstance();
        	
        	if ( childIds == null || childIds.length == 0 )
        	{
        		delegate.delete( new AlleyPrimaryKey( alleyId ) );
        		LOGGER.info( "AlleyController:delete() - successfully deleted Alley with key " + alley.getAlleyPrimaryKey().valuesAsCollection() );
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
	                	LOGGER.info( "AlleyController:delete() - " + exc.getMessage() );
	                	return false;
	                }
        		}
        	}
        }
        catch( Throwable exc )
        {
        	LOGGER.info( "AlleyController:delete() - " + exc.getMessage() );
        	return false;        	
        }
        
        return true;
	}        
	
    /**
     * Handles loading a Alley BO
     * @param		Long alleyId
     * @return		Alley
     */    
    @RequestMapping("/load")
    public Alley load( @RequestParam(value="alley.alleyId", required=true) Long alleyId )
    {    	
        AlleyPrimaryKey pk = null;

    	try
        {  
    		System.out.println( "\n\n****Alley.load pk is " + alleyId );
        	if ( alleyId != null )
        	{
        		pk = new AlleyPrimaryKey( alleyId );
        		
        		// load the Alley
	            this.alley = AlleyBusinessDelegate.getAlleyInstance().getAlley( pk );
	            
	            LOGGER.info( "AlleyController:load() - successfully loaded - " + this.alley.toString() );             
			}
			else
			{
	            LOGGER.info( "AlleyController:load() - unable to locate the primary key as an attribute or a selection for - " + alley.toString() );
	            return null;
			}	            
        }
        catch( Throwable exc )
        {
            LOGGER.info( "AlleyController:load() - failed to load Alley using Id " + alleyId + ", " + exc.getMessage() );
            return null;
        }

        return alley;

    }

    /**
     * Handles loading all Alley business objects
     * @return		List<Alley>
     */
    @RequestMapping("/loadAll")
    public List<Alley> loadAll()
    {                
        List<Alley> alleyList = null;
        
    	try
        {                        
            // load the Alley
            alleyList = AlleyBusinessDelegate.getAlleyInstance().getAllAlley();
            
            if ( alleyList != null )
                LOGGER.info(  "AlleyController:loadAllAlley() - successfully loaded all Alleys" );
        }
        catch( Throwable exc )
        {
            LOGGER.info(  "AlleyController:loadAll() - failed to load all Alleys - " + exc.getMessage() );
        	return null;
            
        }

        return alleyList;
                            
    }


// findAllBy methods



    /**
     * save Leagues on Alley
     * @param		Long alleyId
     * @param		Long childId
     * @param		Long[] childIds
     * @return		Alley
     */     
	@RequestMapping("/saveLeagues")
	public Alley saveLeagues( @RequestParam(value="alley.alleyId", required=false) Long alleyId, 
											@RequestParam(value="childIds", required=false) Long childId, @RequestParam("") Long[] childIds )
	{
		if ( load( alleyId ) == null )
			return( null );
		
		LeaguePrimaryKey pk 					= null;
		League child							= null;
		LeagueBusinessDelegate childDelegate 	= LeagueBusinessDelegate.getLeagueInstance();
		AlleyBusinessDelegate parentDelegate = AlleyBusinessDelegate.getAlleyInstance();		
		
		if ( childId != null || childIds.length == 0 )// creating or saving one
		{
			pk = new LeaguePrimaryKey( childId );
			
			try
			{
				// find the League
				child = childDelegate.getLeague( pk );
			}
			catch( Exception exc )
			{
				LOGGER.info( "AlleyController:saveLeagues() failed get child League using id " + childId  + "- " + exc.getMessage() );
				return( null );
			}
			
			// add it to the Leagues 
			alley.getLeagues().add( child );				
		}
		else
		{
			// clear out the Leagues but 
			alley.getLeagues().clear();
			
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
						LOGGER.info( "AlleyController:saveLeagues() failed get child League using id " + id  + "- " + exc.getMessage() );
					}
				}
			}
		}

		try
		{
			// save the Alley
			parentDelegate.saveAlley( alley );
		}
		catch( Exception exc )
		{
			LOGGER.info( "AlleyController:saveLeagues() failed saving parent Alley - " + exc.getMessage() );
		}

		return alley;
	}

    /**
     * delete Leagues on Alley
     * @param		Long alleyId
     * @param		Long[] childIds
     * @return		Alley
     */     	
	@RequestMapping("/deleteLeagues")
	public Alley deleteLeagues( @RequestParam(value="alley.alleyId", required=true) Long alleyId, 
											@RequestParam(value="childIds", required=false) Long[] childIds )
	{		
		if ( load( alleyId ) == null )
			return( null );

		if ( childIds != null || childIds.length == 0 )
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
					LOGGER.info( "AlleyController:deleteLeagues() failed - " + exc.getMessage() );
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
				LOGGER.info( "AlleyController:deleteLeagues() failed to save the Alley - " + exc.getMessage() );
			}
		}
		
		return alley;
	}

    /**
     * save Tournaments on Alley
     * @param		Long alleyId
     * @param		Long childId
     * @param		Long[] childIds
     * @return		Alley
     */     
	@RequestMapping("/saveTournaments")
	public Alley saveTournaments( @RequestParam(value="alley.alleyId", required=false) Long alleyId, 
											@RequestParam(value="childIds", required=false) Long childId, @RequestParam("") Long[] childIds )
	{
		if ( load( alleyId ) == null )
			return( null );
		
		TournamentPrimaryKey pk 					= null;
		Tournament child							= null;
		TournamentBusinessDelegate childDelegate 	= TournamentBusinessDelegate.getTournamentInstance();
		AlleyBusinessDelegate parentDelegate = AlleyBusinessDelegate.getAlleyInstance();		
		
		if ( childId != null || childIds.length == 0 )// creating or saving one
		{
			pk = new TournamentPrimaryKey( childId );
			
			try
			{
				// find the Tournament
				child = childDelegate.getTournament( pk );
			}
			catch( Exception exc )
			{
				LOGGER.info( "AlleyController:saveTournaments() failed get child Tournament using id " + childId  + "- " + exc.getMessage() );
				return( null );
			}
			
			// add it to the Tournaments 
			alley.getTournaments().add( child );				
		}
		else
		{
			// clear out the Tournaments but 
			alley.getTournaments().clear();
			
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
						LOGGER.info( "AlleyController:saveTournaments() failed get child Tournament using id " + id  + "- " + exc.getMessage() );
					}
				}
			}
		}

		try
		{
			// save the Alley
			parentDelegate.saveAlley( alley );
		}
		catch( Exception exc )
		{
			LOGGER.info( "AlleyController:saveTournaments() failed saving parent Alley - " + exc.getMessage() );
		}

		return alley;
	}

    /**
     * delete Tournaments on Alley
     * @param		Long alleyId
     * @param		Long[] childIds
     * @return		Alley
     */     	
	@RequestMapping("/deleteTournaments")
	public Alley deleteTournaments( @RequestParam(value="alley.alleyId", required=true) Long alleyId, 
											@RequestParam(value="childIds", required=false) Long[] childIds )
	{		
		if ( load( alleyId ) == null )
			return( null );

		if ( childIds != null || childIds.length == 0 )
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
					LOGGER.info( "AlleyController:deleteTournaments() failed - " + exc.getMessage() );
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
				LOGGER.info( "AlleyController:deleteTournaments() failed to save the Alley - " + exc.getMessage() );
			}
		}
		
		return alley;
	}

    /**
     * save Lanes on Alley
     * @param		Long alleyId
     * @param		Long childId
     * @param		Long[] childIds
     * @return		Alley
     */     
	@RequestMapping("/saveLanes")
	public Alley saveLanes( @RequestParam(value="alley.alleyId", required=false) Long alleyId, 
											@RequestParam(value="childIds", required=false) Long childId, @RequestParam("") Long[] childIds )
	{
		if ( load( alleyId ) == null )
			return( null );
		
		LanePrimaryKey pk 					= null;
		Lane child							= null;
		LaneBusinessDelegate childDelegate 	= LaneBusinessDelegate.getLaneInstance();
		AlleyBusinessDelegate parentDelegate = AlleyBusinessDelegate.getAlleyInstance();		
		
		if ( childId != null || childIds.length == 0 )// creating or saving one
		{
			pk = new LanePrimaryKey( childId );
			
			try
			{
				// find the Lane
				child = childDelegate.getLane( pk );
			}
			catch( Exception exc )
			{
				LOGGER.info( "AlleyController:saveLanes() failed get child Lane using id " + childId  + "- " + exc.getMessage() );
				return( null );
			}
			
			// add it to the Lanes 
			alley.getLanes().add( child );				
		}
		else
		{
			// clear out the Lanes but 
			alley.getLanes().clear();
			
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
						LOGGER.info( "AlleyController:saveLanes() failed get child Lane using id " + id  + "- " + exc.getMessage() );
					}
				}
			}
		}

		try
		{
			// save the Alley
			parentDelegate.saveAlley( alley );
		}
		catch( Exception exc )
		{
			LOGGER.info( "AlleyController:saveLanes() failed saving parent Alley - " + exc.getMessage() );
		}

		return alley;
	}

    /**
     * delete Lanes on Alley
     * @param		Long alleyId
     * @param		Long[] childIds
     * @return		Alley
     */     	
	@RequestMapping("/deleteLanes")
	public Alley deleteLanes( @RequestParam(value="alley.alleyId", required=true) Long alleyId, 
											@RequestParam(value="childIds", required=false) Long[] childIds )
	{		
		if ( load( alleyId ) == null )
			return( null );

		if ( childIds != null || childIds.length == 0 )
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
					LOGGER.info( "AlleyController:deleteLanes() failed - " + exc.getMessage() );
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
				LOGGER.info( "AlleyController:deleteLanes() failed to save the Alley - " + exc.getMessage() );
			}
		}
		
		return alley;
	}


    /**
     * Handles creating a Alley BO
     * @return		Alley
     */
    protected Alley create()
    {
        try
        {       
			this.alley = AlleyBusinessDelegate.getAlleyInstance().createAlley( alley );
        }
        catch( Throwable exc )
        {
        	LOGGER.info( "AlleyController:create() - exception Alley - " + exc.getMessage());        	
        	return null;
        }
        
        return this.alley;
    }

    /**
     * Handles updating a Alley BO
     * @return		Alley
     */    
    protected Alley update()
    {
    	// store provided data
        Alley tmp = alley;

        // load actual data from db
    	load();
    	
    	// copy provided data into actual data
    	alley.copyShallow( tmp );
    	
        try
        {                        	        
			// create the AlleyBusiness Delegate            
			AlleyBusinessDelegate delegate = AlleyBusinessDelegate.getAlleyInstance();
            this.alley = delegate.saveAlley( alley );
            
            if ( this.alley != null )
                LOGGER.info( "AlleyController:update() - successfully updated Alley - " + alley.toString() );
        }
        catch( Throwable exc )
        {
        	LOGGER.info( "AlleyController:update() - successfully update Alley - " + exc.getMessage());        	
        	return null;
        }
        
        return this.alley;
        
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

    protected Alley load()
    {
    	return( load( new Long( alley.getAlleyPrimaryKey().getFirstKey().toString() ) ));
    }

//************************************************************************    
// Attributes
//************************************************************************
    protected Alley alley = null;
    private static final Logger LOGGER = Logger.getLogger(Alley.class.getName());
    
}


