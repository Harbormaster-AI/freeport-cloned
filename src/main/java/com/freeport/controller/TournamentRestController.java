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
 * Implements Struts action processing for business entity Tournament.
 *
 * @author Dev Team
 */
@RestController
@RequestMapping("/Tournament")
public class TournamentRestController extends BaseSpringRestController
{

    /**
     * Handles saving a Tournament BO.  if not key provided, calls create, otherwise calls save
     * @param		Tournament tournament
     * @return		Tournament
     */
	@RequestMapping("/save")
    public Tournament save( @RequestBody Tournament tournament )
    {
    	// assign locally
    	this.tournament = tournament;
    	
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
     * Handles deleting a Tournament BO
     * @param		Long tournamentId
     * @param 		Long[] childIds
     * @return		boolean
     */
    @RequestMapping("/delete")    
    public boolean delete( @RequestParam(value="tournament.tournamentId", required=false) Long tournamentId, 
    						@RequestParam(value="childIds", required=false) Long[] childIds )
    {                
        try
        {
        	TournamentBusinessDelegate delegate = TournamentBusinessDelegate.getTournamentInstance();
        	
        	if ( childIds == null || childIds.length == 0 )
        	{
        		delegate.delete( new TournamentPrimaryKey( tournamentId ) );
        		LOGGER.info( "TournamentController:delete() - successfully deleted Tournament with key " + tournament.getTournamentPrimaryKey().valuesAsCollection() );
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
	                	LOGGER.info( "TournamentController:delete() - " + exc.getMessage() );
	                	return false;
	                }
        		}
        	}
        }
        catch( Throwable exc )
        {
        	LOGGER.info( "TournamentController:delete() - " + exc.getMessage() );
        	return false;        	
        }
        
        return true;
	}        
	
    /**
     * Handles loading a Tournament BO
     * @param		Long tournamentId
     * @return		Tournament
     */    
    @RequestMapping("/load")
    public Tournament load( @RequestParam(value="tournament.tournamentId", required=true) Long tournamentId )
    {    	
        TournamentPrimaryKey pk = null;

    	try
        {  
    		System.out.println( "\n\n****Tournament.load pk is " + tournamentId );
        	if ( tournamentId != null )
        	{
        		pk = new TournamentPrimaryKey( tournamentId );
        		
        		// load the Tournament
	            this.tournament = TournamentBusinessDelegate.getTournamentInstance().getTournament( pk );
	            
	            LOGGER.info( "TournamentController:load() - successfully loaded - " + this.tournament.toString() );             
			}
			else
			{
	            LOGGER.info( "TournamentController:load() - unable to locate the primary key as an attribute or a selection for - " + tournament.toString() );
	            return null;
			}	            
        }
        catch( Throwable exc )
        {
            LOGGER.info( "TournamentController:load() - failed to load Tournament using Id " + tournamentId + ", " + exc.getMessage() );
            return null;
        }

        return tournament;

    }

    /**
     * Handles loading all Tournament business objects
     * @return		List<Tournament>
     */
    @RequestMapping("/loadAll")
    public List<Tournament> loadAll()
    {                
        List<Tournament> tournamentList = null;
        
    	try
        {                        
            // load the Tournament
            tournamentList = TournamentBusinessDelegate.getTournamentInstance().getAllTournament();
            
            if ( tournamentList != null )
                LOGGER.info(  "TournamentController:loadAllTournament() - successfully loaded all Tournaments" );
        }
        catch( Throwable exc )
        {
            LOGGER.info(  "TournamentController:loadAll() - failed to load all Tournaments - " + exc.getMessage() );
        	return null;
            
        }

        return tournamentList;
                            
    }


// findAllBy methods



    /**
     * save Matchups on Tournament
     * @param		Long tournamentId
     * @param		Long childId
     * @param		Long[] childIds
     * @return		Tournament
     */     
	@RequestMapping("/saveMatchups")
	public Tournament saveMatchups( @RequestParam(value="tournament.tournamentId", required=false) Long tournamentId, 
											@RequestParam(value="childIds", required=false) Long childId, @RequestParam("") Long[] childIds )
	{
		if ( load( tournamentId ) == null )
			return( null );
		
		MatchupPrimaryKey pk 					= null;
		Matchup child							= null;
		MatchupBusinessDelegate childDelegate 	= MatchupBusinessDelegate.getMatchupInstance();
		TournamentBusinessDelegate parentDelegate = TournamentBusinessDelegate.getTournamentInstance();		
		
		if ( childId != null || childIds.length == 0 )// creating or saving one
		{
			pk = new MatchupPrimaryKey( childId );
			
			try
			{
				// find the Matchup
				child = childDelegate.getMatchup( pk );
			}
			catch( Exception exc )
			{
				LOGGER.info( "TournamentController:saveMatchups() failed get child Matchup using id " + childId  + "- " + exc.getMessage() );
				return( null );
			}
			
			// add it to the Matchups 
			tournament.getMatchups().add( child );				
		}
		else
		{
			// clear out the Matchups but 
			tournament.getMatchups().clear();
			
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
						LOGGER.info( "TournamentController:saveMatchups() failed get child Matchup using id " + id  + "- " + exc.getMessage() );
					}
				}
			}
		}

		try
		{
			// save the Tournament
			parentDelegate.saveTournament( tournament );
		}
		catch( Exception exc )
		{
			LOGGER.info( "TournamentController:saveMatchups() failed saving parent Tournament - " + exc.getMessage() );
		}

		return tournament;
	}

    /**
     * delete Matchups on Tournament
     * @param		Long tournamentId
     * @param		Long[] childIds
     * @return		Tournament
     */     	
	@RequestMapping("/deleteMatchups")
	public Tournament deleteMatchups( @RequestParam(value="tournament.tournamentId", required=true) Long tournamentId, 
											@RequestParam(value="childIds", required=false) Long[] childIds )
	{		
		if ( load( tournamentId ) == null )
			return( null );

		if ( childIds != null || childIds.length == 0 )
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
					LOGGER.info( "TournamentController:deleteMatchups() failed - " + exc.getMessage() );
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
				LOGGER.info( "TournamentController:deleteMatchups() failed to save the Tournament - " + exc.getMessage() );
			}
		}
		
		return tournament;
	}


    /**
     * Handles creating a Tournament BO
     * @return		Tournament
     */
    protected Tournament create()
    {
        try
        {       
			this.tournament = TournamentBusinessDelegate.getTournamentInstance().createTournament( tournament );
        }
        catch( Throwable exc )
        {
        	LOGGER.info( "TournamentController:create() - exception Tournament - " + exc.getMessage());        	
        	return null;
        }
        
        return this.tournament;
    }

    /**
     * Handles updating a Tournament BO
     * @return		Tournament
     */    
    protected Tournament update()
    {
    	// store provided data
        Tournament tmp = tournament;

        // load actual data from db
    	load();
    	
    	// copy provided data into actual data
    	tournament.copyShallow( tmp );
    	
        try
        {                        	        
			// create the TournamentBusiness Delegate            
			TournamentBusinessDelegate delegate = TournamentBusinessDelegate.getTournamentInstance();
            this.tournament = delegate.saveTournament( tournament );
            
            if ( this.tournament != null )
                LOGGER.info( "TournamentController:update() - successfully updated Tournament - " + tournament.toString() );
        }
        catch( Throwable exc )
        {
        	LOGGER.info( "TournamentController:update() - successfully update Tournament - " + exc.getMessage());        	
        	return null;
        }
        
        return this.tournament;
        
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

    protected Tournament load()
    {
    	return( load( new Long( tournament.getTournamentPrimaryKey().getFirstKey().toString() ) ));
    }

//************************************************************************    
// Attributes
//************************************************************************
    protected Tournament tournament = null;
    private static final Logger LOGGER = Logger.getLogger(Tournament.class.getName());
    
}


