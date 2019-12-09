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
 * Implements Struts action processing for business entity Matchup.
 *
 * @author Dev Team
 */
@RestController
@RequestMapping("/Matchup")
public class MatchupRestController extends BaseSpringRestController
{

    /**
     * Handles saving a Matchup BO.  if not key provided, calls create, otherwise calls save
     * @param		Matchup matchup
     * @return		Matchup
     */
	@RequestMapping("/save")
    public Matchup save( @RequestBody Matchup matchup )
    {
    	// assign locally
    	this.matchup = matchup;
    	
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
     * Handles deleting a Matchup BO
     * @param		Long matchupId
     * @param 		Long[] childIds
     * @return		boolean
     */
    @RequestMapping("/delete")    
    public boolean delete( @RequestParam(value="matchup.matchupId", required=false) Long matchupId, 
    						@RequestParam(value="childIds", required=false) Long[] childIds )
    {                
        try
        {
        	MatchupBusinessDelegate delegate = MatchupBusinessDelegate.getMatchupInstance();
        	
        	if ( childIds == null || childIds.length == 0 )
        	{
        		delegate.delete( new MatchupPrimaryKey( matchupId ) );
        		LOGGER.info( "MatchupController:delete() - successfully deleted Matchup with key " + matchup.getMatchupPrimaryKey().valuesAsCollection() );
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
	                	LOGGER.info( "MatchupController:delete() - " + exc.getMessage() );
	                	return false;
	                }
        		}
        	}
        }
        catch( Throwable exc )
        {
        	LOGGER.info( "MatchupController:delete() - " + exc.getMessage() );
        	return false;        	
        }
        
        return true;
	}        
	
    /**
     * Handles loading a Matchup BO
     * @param		Long matchupId
     * @return		Matchup
     */    
    @RequestMapping("/load")
    public Matchup load( @RequestParam(value="matchup.matchupId", required=true) Long matchupId )
    {    	
        MatchupPrimaryKey pk = null;

    	try
        {  
    		System.out.println( "\n\n****Matchup.load pk is " + matchupId );
        	if ( matchupId != null )
        	{
        		pk = new MatchupPrimaryKey( matchupId );
        		
        		// load the Matchup
	            this.matchup = MatchupBusinessDelegate.getMatchupInstance().getMatchup( pk );
	            
	            LOGGER.info( "MatchupController:load() - successfully loaded - " + this.matchup.toString() );             
			}
			else
			{
	            LOGGER.info( "MatchupController:load() - unable to locate the primary key as an attribute or a selection for - " + matchup.toString() );
	            return null;
			}	            
        }
        catch( Throwable exc )
        {
            LOGGER.info( "MatchupController:load() - failed to load Matchup using Id " + matchupId + ", " + exc.getMessage() );
            return null;
        }

        return matchup;

    }

    /**
     * Handles loading all Matchup business objects
     * @return		List<Matchup>
     */
    @RequestMapping("/loadAll")
    public List<Matchup> loadAll()
    {                
        List<Matchup> matchupList = null;
        
    	try
        {                        
            // load the Matchup
            matchupList = MatchupBusinessDelegate.getMatchupInstance().getAllMatchup();
            
            if ( matchupList != null )
                LOGGER.info(  "MatchupController:loadAllMatchup() - successfully loaded all Matchups" );
        }
        catch( Throwable exc )
        {
            LOGGER.info(  "MatchupController:loadAll() - failed to load all Matchups - " + exc.getMessage() );
        	return null;
            
        }

        return matchupList;
                            
    }


// findAllBy methods



    /**
     * save Games on Matchup
     * @param		Long matchupId
     * @param		Long childId
     * @param		Long[] childIds
     * @return		Matchup
     */     
	@RequestMapping("/saveGames")
	public Matchup saveGames( @RequestParam(value="matchup.matchupId", required=false) Long matchupId, 
											@RequestParam(value="childIds", required=false) Long childId, @RequestParam("") Long[] childIds )
	{
		if ( load( matchupId ) == null )
			return( null );
		
		GamePrimaryKey pk 					= null;
		Game child							= null;
		GameBusinessDelegate childDelegate 	= GameBusinessDelegate.getGameInstance();
		MatchupBusinessDelegate parentDelegate = MatchupBusinessDelegate.getMatchupInstance();		
		
		if ( childId != null || childIds.length == 0 )// creating or saving one
		{
			pk = new GamePrimaryKey( childId );
			
			try
			{
				// find the Game
				child = childDelegate.getGame( pk );
			}
			catch( Exception exc )
			{
				LOGGER.info( "MatchupController:saveGames() failed get child Game using id " + childId  + "- " + exc.getMessage() );
				return( null );
			}
			
			// add it to the Games 
			matchup.getGames().add( child );				
		}
		else
		{
			// clear out the Games but 
			matchup.getGames().clear();
			
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
						LOGGER.info( "MatchupController:saveGames() failed get child Game using id " + id  + "- " + exc.getMessage() );
					}
				}
			}
		}

		try
		{
			// save the Matchup
			parentDelegate.saveMatchup( matchup );
		}
		catch( Exception exc )
		{
			LOGGER.info( "MatchupController:saveGames() failed saving parent Matchup - " + exc.getMessage() );
		}

		return matchup;
	}

    /**
     * delete Games on Matchup
     * @param		Long matchupId
     * @param		Long[] childIds
     * @return		Matchup
     */     	
	@RequestMapping("/deleteGames")
	public Matchup deleteGames( @RequestParam(value="matchup.matchupId", required=true) Long matchupId, 
											@RequestParam(value="childIds", required=false) Long[] childIds )
	{		
		if ( load( matchupId ) == null )
			return( null );

		if ( childIds != null || childIds.length == 0 )
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
					LOGGER.info( "MatchupController:deleteGames() failed - " + exc.getMessage() );
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
				LOGGER.info( "MatchupController:deleteGames() failed to save the Matchup - " + exc.getMessage() );
			}
		}
		
		return matchup;
	}


    /**
     * Handles creating a Matchup BO
     * @return		Matchup
     */
    protected Matchup create()
    {
        try
        {       
			this.matchup = MatchupBusinessDelegate.getMatchupInstance().createMatchup( matchup );
        }
        catch( Throwable exc )
        {
        	LOGGER.info( "MatchupController:create() - exception Matchup - " + exc.getMessage());        	
        	return null;
        }
        
        return this.matchup;
    }

    /**
     * Handles updating a Matchup BO
     * @return		Matchup
     */    
    protected Matchup update()
    {
    	// store provided data
        Matchup tmp = matchup;

        // load actual data from db
    	load();
    	
    	// copy provided data into actual data
    	matchup.copyShallow( tmp );
    	
        try
        {                        	        
			// create the MatchupBusiness Delegate            
			MatchupBusinessDelegate delegate = MatchupBusinessDelegate.getMatchupInstance();
            this.matchup = delegate.saveMatchup( matchup );
            
            if ( this.matchup != null )
                LOGGER.info( "MatchupController:update() - successfully updated Matchup - " + matchup.toString() );
        }
        catch( Throwable exc )
        {
        	LOGGER.info( "MatchupController:update() - successfully update Matchup - " + exc.getMessage());        	
        	return null;
        }
        
        return this.matchup;
        
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

    protected Matchup load()
    {
    	return( load( new Long( matchup.getMatchupPrimaryKey().getFirstKey().toString() ) ));
    }

//************************************************************************    
// Attributes
//************************************************************************
    protected Matchup matchup = null;
    private static final Logger LOGGER = Logger.getLogger(Matchup.class.getName());
    
}


