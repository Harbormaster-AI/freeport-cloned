/*******************************************************************************
  Turnstone Biologics Confidential
  
  2018 Turnstone Biologics
  All Rights Reserved.
  
  This file is subject to the terms and conditions defined in
  file 'license.txt', which is part of this source code package.
   
  Contributors :
        Turnstone Biologics - General Release
 ******************************************************************************/
package com.freeport.delegate;

import java.util.*;
import java.io.IOException;

//import java.util.logging.Level;
//import java.util.logging.Logger;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.*;

import io.swagger.annotations.*;

import com.amazonaws.services.lambda.runtime.Context;

    import com.freeport.primarykey.*;
    import com.freeport.dao.*;
    import com.freeport.bo.*;
    
import com.freeport.exception.CreationException;
import com.freeport.exception.DeletionException;
import com.freeport.exception.NotFoundException;
import com.freeport.exception.SaveException;

/**
 * Tournament AWS Lambda Proxy delegate class.
 * <p>
 * This class implements the Business Delegate design pattern for the purpose of:
 * <ol>
 * <li>Reducing coupling between the business tier and a client of the business tier by hiding all business-tier implementation details</li>
 * <li>Improving the available of Tournament related services in the case of a Tournament business related service failing.</li>
 * <li>Exposes a simpler, uniform Tournament interface to the business tier, making it easy for clients to consume a simple Java object.</li>
 * <li>Hides the communication protocol that may be required to fulfill Tournament business related services.</li>
 * </ol>
 * <p>
 * @author Dev Team
 */
@Api(value = "Tournament", description = "RESTful API to interact with Tournament resources.")
@Path("/Tournament")
public class TournamentAWSLambdaDelegate 
extends BaseAWSLambdaDelegate
{
//************************************************************************
// Public Methods
//************************************************************************
    /** 
     * Default Constructor 
     */
    public TournamentAWSLambdaDelegate() {
	}


    /**
     * Creates the provided Tournament
     * @param		businessObject 	Tournament
	 * @param		context		Context	
     * @return     	Tournament
     * @exception   CreationException
     */
    @ApiOperation(value = "Creates a Tournament", notes = "Creates Tournament using the provided data" )
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    public static Tournament createTournament( 
    		@ApiParam(value = "Tournament entity to create", required = true) Tournament businessObject, 
    		Context context ) 
    	throws CreationException {
    	
		if ( businessObject == null )
        {
            String errMsg = "Null Tournament provided but not allowed " + getContextDetails(context);
            context.getLogger().log( errMsg );
            throw new CreationException( errMsg ); 
        }
      
        // return value once persisted
        TournamentDAO dao  = getTournamentDAO();
        
        try
        {
            businessObject = dao.createTournament( businessObject );
        }
        catch (Exception exc)
        {
        	String errMsg = "TournamentAWSLambdaDelegate:createTournament() - Unable to create Tournament" + getContextDetails(context) + exc;
        	context.getLogger().log( errMsg );
            throw new CreationException( errMsg );
        }
        finally
        {
            releaseTournamentDAO( dao );            
        }        
         
        return( businessObject );
         
     }

    /**
     * Method to retrieve the Tournament via a supplied TournamentPrimaryKey.
     * @param 	key
	 * @param	context		Context
     * @return 	Tournament
     * @exception NotFoundException - Thrown if processing any related problems
     */
    @ApiOperation(value = "Gets a Tournament", notes = "Gets the Tournament associated with the provided primary key", response = Tournament.class)
    @GET
    @Path("/find")
    @Produces(MediaType.APPLICATION_JSON)    
    public static Tournament getTournament( 
    		@ApiParam(value = "Tournament primary key", required = true) TournamentPrimaryKey key, 
    		Context context  ) 
    	throws NotFoundException {
        
        Tournament businessObject  	= null;                
        TournamentDAO dao 			= getTournamentDAO();
            
        try
        {
        	businessObject = dao.findTournament( key );
        }
        catch( Exception exc )
        {
            String errMsg = "Unable to locate Tournament with key " + key.toString() + " - " + getContextDetails(context) + exc;
            context.getLogger().log( errMsg );
            throw new NotFoundException( errMsg );
        }
        finally
        {
            releaseTournamentDAO( dao );
        }        
        
        return businessObject;
    }
     
   /**
    * Saves the provided Tournament
    * @param		businessObject		Tournament
	* @param		context		Context	
    * @return       what was just saved
    * @exception    SaveException
    */
    @ApiOperation(value = "Saves a Tournament", notes = "Saves Tournament using the provided data" )
    @PUT
    @Path("/save")
    @Consumes(MediaType.APPLICATION_JSON)
    public static Tournament saveTournament( 
    		@ApiParam(value = "Tournament entity to save", required = true) Tournament businessObject, Context context  ) 
    	throws SaveException {

    	if ( businessObject == null )
        {
            String errMsg = "Null Tournament provided but not allowed " + getContextDetails(context);
            context.getLogger().log( errMsg );
            throw new SaveException( errMsg ); 
        }
    	
        // --------------------------------
        // If the businessObject has a key, find it and apply the businessObject
        // --------------------------------
        TournamentPrimaryKey key = businessObject.getTournamentPrimaryKey();
                    
        if ( key != null )
        {
            TournamentDAO dao = getTournamentDAO();

            try
            {                    
                businessObject = (Tournament)dao.saveTournament( businessObject );
            }
            catch (Exception exc)
            {
                String errMsg = "Unable to save Tournament" + getContextDetails(context) + exc;
                context.getLogger().log( errMsg );
                throw new SaveException( errMsg );
            }
            finally
            {
            	releaseTournamentDAO( dao );
            }
        }
        else
        {
            String errMsg = "Unable to create Tournament due to it having a null TournamentPrimaryKey."; 
            context.getLogger().log( errMsg );
            throw new SaveException( errMsg );
        }
		        
        return( businessObject );
        
    }
     

	/**
     * Method to retrieve a collection of all Tournaments
     * @param		context		Context
     * @return 	ArrayList<Tournament> 
     */
    @ApiOperation(value = "Get all Tournament", notes = "Get all Tournament from storage", responseContainer = "ArrayList", response = Tournament.class)
    @GET
    @Path("/getAll")
    @Produces(MediaType.APPLICATION_JSON)    
    public static ArrayList<Tournament> getAllTournament( Context context ) 
    	throws NotFoundException {

        ArrayList<Tournament> array	= null;
        TournamentDAO dao 			= getTournamentDAO();
        
        try
        {
            array = dao.findAllTournament();
        }
        catch( Exception exc )
        {
            String errMsg = "failed to getAllTournament - " + getContextDetails(context) + exc.getMessage();
            context.getLogger().log( errMsg );
            throw new NotFoundException( errMsg );
        }
        finally
        {
        	releaseTournamentDAO( dao );
        }        
        
        return array;
    }
           
     
    /**
     * Deletes the associated business object using the provided primary key.
     * @param		key 	TournamentPrimaryKey
     * @param		context		Context    
     * @exception 	DeletionException
     */
    @ApiOperation(value = "Deletes a Tournament", notes = "Deletes the Tournament associated with the provided primary key", response = Tournament.class)
    @DELETE
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_JSON)    
	public static void deleteTournament( 
			@ApiParam(value = "Tournament primary key", required = true) TournamentPrimaryKey key, 
			Context context  ) 
    	throws DeletionException {    	

    	if ( key == null )
        {
            String errMsg = "Null key provided but not allowed " + getContextDetails(context) ;
            context.getLogger().log( errMsg );
            throw new DeletionException( errMsg );
        }

        TournamentDAO dao  = getTournamentDAO();

		boolean deleted = false;
		
        try
        {                    
            deleted = dao.deleteTournament( key );
        }
        catch (Exception exc)
        {
        	String errMsg = "Unable to delete Tournament using key = "  + key + ". " + getContextDetails(context) + exc;
        	context.getLogger().log( errMsg );
            throw new DeletionException( errMsg );
        }
        finally
        {
            releaseTournamentDAO( dao );
        }
         		
        return;
     }

// role related methods

 
    /**
     * Retrieves the Matchups on a Tournament
     * @param		parentKey	TournamentPrimaryKey
     * @param		context		Context
     * @return    	Set<Matchup>
     * @exception	NotFoundException
     */
	public static Set<Matchup> getMatchups( TournamentPrimaryKey parentKey, Context context )
		throws NotFoundException
    {
    	Tournament tournament 	= getTournament( parentKey, context );
    	return (tournament.getMatchups());
    }
    
    /**
     * Add the assigned Matchup into the Matchups of the relevant Tournament
     * @param		parentKey	TournamentPrimaryKey
     * @param		childKey	MatchupPrimaryKey
	 * @param		context		Context
     * @return    	Tournament
     * @exception	NotFoundException
     */
	public static Tournament addMatchups( 	TournamentPrimaryKey parentKey, 
									MatchupPrimaryKey childKey, 
									Context context )
	throws SaveException, NotFoundException
	{
		Tournament tournament 	= getTournament( parentKey, context );

		// find the Matchup
		Matchup child = MatchupAWSLambdaDelegate.getMatchup( childKey, context );
		
		// add it to the Matchups 
		tournament.getMatchups().add( child );				
		
		// save the Tournament
		tournament = TournamentAWSLambdaDelegate.saveTournament( tournament, context );

		return ( tournament );
	}

    /**
     * Saves multiple Matchup entities as the Matchups to the relevant Tournament
     * @param		parentKey	TournamentPrimaryKey
     * @param		List<MatchupPrimaryKey> childKeys
     * @return    	Tournament
     * @exception	SaveException
     * @exception	NotFoundException
     */
	public Tournament assignMatchups( TournamentPrimaryKey parentKey, 
											List<MatchupPrimaryKey> childKeys, 
											Context context )
		throws SaveException, NotFoundException {

		Tournament tournament 	= getTournament( parentKey, context );
		
		// clear out the Matchups 
		tournament.getMatchups().clear();
		
		// finally, find each child and add
		if ( childKeys != null )
		{
			Matchup child = null;
			for( MatchupPrimaryKey childKey : childKeys )
			{
				// retrieve the Matchup
				child = MatchupAWSLambdaDelegate.getMatchup( childKey, context );

				// add it to the Matchups List
				tournament.getMatchups().add( child );
			}
		}
		
		// save the Tournament
		tournament = TournamentAWSLambdaDelegate.saveTournament( tournament, context );

		return( tournament );
	}

    /**
     * Delete multiple Matchup entities as the Matchups to the relevant Tournament
     * @param		parentKey	TournamentPrimaryKey
     * @param		List<MatchupPrimaryKey> childKeys
     * @return    	Tournament
     * @exception	DeletionException
     * @exception	NotFoundException
     * @exception	SaveException
     */
	public Tournament deleteMatchups( TournamentPrimaryKey parentKey, 
											List<MatchupPrimaryKey> childKeys, 
											Context context )
		throws DeletionException, NotFoundException, SaveException {		
		Tournament tournament 	= getTournament( parentKey, context );

		if ( childKeys != null )
		{
			Set<Matchup> children	= tournament.getMatchups();
			Matchup child 			= null;
			
			for( MatchupPrimaryKey childKey : childKeys )
			{
				try
				{
					// first remove the relevant child from the list
					child = MatchupAWSLambdaDelegate.getMatchup( childKey, context );
					children.remove( child );
					
					// then safe to delete the child				
					MatchupAWSLambdaDelegate.deleteMatchup( childKey, context );
				}
				catch( Exception exc )
				{
					String errMsg = "Deletion failed - " + exc.getMessage();
					context.getLogger().log( errMsg );
					throw new DeletionException( errMsg );
				}
			}
			
			// assign the modified list of Matchup back to the tournament
			tournament.setMatchups( children );			
			// save it 
			tournament = TournamentAWSLambdaDelegate.saveTournament( tournament, context );
		}
		
		return ( tournament );
	}

 	
    /**
     * Returns the Tournament specific DAO.
     *
     * @return      Tournament DAO
     */
    public static TournamentDAO getTournamentDAO()
    {
        return( new com.freeport.dao.TournamentDAO() ); 
    }

    /**
     * Release the TournamentDAO back to the FrameworkDAOFactory
     */
    public static void releaseTournamentDAO( com.freeport.dao.TournamentDAO dao )
    {
        dao = null;
    }
    
//************************************************************************
// Attributes
//************************************************************************

//    private static final Logger LOGGER = Logger.getLogger(TournamentAWSLambdaDelegate.class.getName());
}

