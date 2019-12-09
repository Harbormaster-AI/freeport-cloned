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
 * League AWS Lambda Proxy delegate class.
 * <p>
 * This class implements the Business Delegate design pattern for the purpose of:
 * <ol>
 * <li>Reducing coupling between the business tier and a client of the business tier by hiding all business-tier implementation details</li>
 * <li>Improving the available of League related services in the case of a League business related service failing.</li>
 * <li>Exposes a simpler, uniform League interface to the business tier, making it easy for clients to consume a simple Java object.</li>
 * <li>Hides the communication protocol that may be required to fulfill League business related services.</li>
 * </ol>
 * <p>
 * @author Dev Team
 */
@Api(value = "League", description = "RESTful API to interact with League resources.")
@Path("/League")
public class LeagueAWSLambdaDelegate 
extends BaseAWSLambdaDelegate
{
//************************************************************************
// Public Methods
//************************************************************************
    /** 
     * Default Constructor 
     */
    public LeagueAWSLambdaDelegate() {
	}


    /**
     * Creates the provided League
     * @param		businessObject 	League
	 * @param		context		Context	
     * @return     	League
     * @exception   CreationException
     */
    @ApiOperation(value = "Creates a League", notes = "Creates League using the provided data" )
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    public static League createLeague( 
    		@ApiParam(value = "League entity to create", required = true) League businessObject, 
    		Context context ) 
    	throws CreationException {
    	
		if ( businessObject == null )
        {
            String errMsg = "Null League provided but not allowed " + getContextDetails(context);
            context.getLogger().log( errMsg );
            throw new CreationException( errMsg ); 
        }
      
        // return value once persisted
        LeagueDAO dao  = getLeagueDAO();
        
        try
        {
            businessObject = dao.createLeague( businessObject );
        }
        catch (Exception exc)
        {
        	String errMsg = "LeagueAWSLambdaDelegate:createLeague() - Unable to create League" + getContextDetails(context) + exc;
        	context.getLogger().log( errMsg );
            throw new CreationException( errMsg );
        }
        finally
        {
            releaseLeagueDAO( dao );            
        }        
         
        return( businessObject );
         
     }

    /**
     * Method to retrieve the League via a supplied LeaguePrimaryKey.
     * @param 	key
	 * @param	context		Context
     * @return 	League
     * @exception NotFoundException - Thrown if processing any related problems
     */
    @ApiOperation(value = "Gets a League", notes = "Gets the League associated with the provided primary key", response = League.class)
    @GET
    @Path("/find")
    @Produces(MediaType.APPLICATION_JSON)    
    public static League getLeague( 
    		@ApiParam(value = "League primary key", required = true) LeaguePrimaryKey key, 
    		Context context  ) 
    	throws NotFoundException {
        
        League businessObject  	= null;                
        LeagueDAO dao 			= getLeagueDAO();
            
        try
        {
        	businessObject = dao.findLeague( key );
        }
        catch( Exception exc )
        {
            String errMsg = "Unable to locate League with key " + key.toString() + " - " + getContextDetails(context) + exc;
            context.getLogger().log( errMsg );
            throw new NotFoundException( errMsg );
        }
        finally
        {
            releaseLeagueDAO( dao );
        }        
        
        return businessObject;
    }
     
   /**
    * Saves the provided League
    * @param		businessObject		League
	* @param		context		Context	
    * @return       what was just saved
    * @exception    SaveException
    */
    @ApiOperation(value = "Saves a League", notes = "Saves League using the provided data" )
    @PUT
    @Path("/save")
    @Consumes(MediaType.APPLICATION_JSON)
    public static League saveLeague( 
    		@ApiParam(value = "League entity to save", required = true) League businessObject, Context context  ) 
    	throws SaveException {

    	if ( businessObject == null )
        {
            String errMsg = "Null League provided but not allowed " + getContextDetails(context);
            context.getLogger().log( errMsg );
            throw new SaveException( errMsg ); 
        }
    	
        // --------------------------------
        // If the businessObject has a key, find it and apply the businessObject
        // --------------------------------
        LeaguePrimaryKey key = businessObject.getLeaguePrimaryKey();
                    
        if ( key != null )
        {
            LeagueDAO dao = getLeagueDAO();

            try
            {                    
                businessObject = (League)dao.saveLeague( businessObject );
            }
            catch (Exception exc)
            {
                String errMsg = "Unable to save League" + getContextDetails(context) + exc;
                context.getLogger().log( errMsg );
                throw new SaveException( errMsg );
            }
            finally
            {
            	releaseLeagueDAO( dao );
            }
        }
        else
        {
            String errMsg = "Unable to create League due to it having a null LeaguePrimaryKey."; 
            context.getLogger().log( errMsg );
            throw new SaveException( errMsg );
        }
		        
        return( businessObject );
        
    }
     

	/**
     * Method to retrieve a collection of all Leagues
     * @param		context		Context
     * @return 	ArrayList<League> 
     */
    @ApiOperation(value = "Get all League", notes = "Get all League from storage", responseContainer = "ArrayList", response = League.class)
    @GET
    @Path("/getAll")
    @Produces(MediaType.APPLICATION_JSON)    
    public static ArrayList<League> getAllLeague( Context context ) 
    	throws NotFoundException {

        ArrayList<League> array	= null;
        LeagueDAO dao 			= getLeagueDAO();
        
        try
        {
            array = dao.findAllLeague();
        }
        catch( Exception exc )
        {
            String errMsg = "failed to getAllLeague - " + getContextDetails(context) + exc.getMessage();
            context.getLogger().log( errMsg );
            throw new NotFoundException( errMsg );
        }
        finally
        {
        	releaseLeagueDAO( dao );
        }        
        
        return array;
    }
           
     
    /**
     * Deletes the associated business object using the provided primary key.
     * @param		key 	LeaguePrimaryKey
     * @param		context		Context    
     * @exception 	DeletionException
     */
    @ApiOperation(value = "Deletes a League", notes = "Deletes the League associated with the provided primary key", response = League.class)
    @DELETE
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_JSON)    
	public static void deleteLeague( 
			@ApiParam(value = "League primary key", required = true) LeaguePrimaryKey key, 
			Context context  ) 
    	throws DeletionException {    	

    	if ( key == null )
        {
            String errMsg = "Null key provided but not allowed " + getContextDetails(context) ;
            context.getLogger().log( errMsg );
            throw new DeletionException( errMsg );
        }

        LeagueDAO dao  = getLeagueDAO();

		boolean deleted = false;
		
        try
        {                    
            deleted = dao.deleteLeague( key );
        }
        catch (Exception exc)
        {
        	String errMsg = "Unable to delete League using key = "  + key + ". " + getContextDetails(context) + exc;
        	context.getLogger().log( errMsg );
            throw new DeletionException( errMsg );
        }
        finally
        {
            releaseLeagueDAO( dao );
        }
         		
        return;
     }

// role related methods

 
    /**
     * Retrieves the Players on a League
     * @param		parentKey	LeaguePrimaryKey
     * @param		context		Context
     * @return    	Set<Player>
     * @exception	NotFoundException
     */
	public static Set<Player> getPlayers( LeaguePrimaryKey parentKey, Context context )
		throws NotFoundException
    {
    	League league 	= getLeague( parentKey, context );
    	return (league.getPlayers());
    }
    
    /**
     * Add the assigned Player into the Players of the relevant League
     * @param		parentKey	LeaguePrimaryKey
     * @param		childKey	PlayerPrimaryKey
	 * @param		context		Context
     * @return    	League
     * @exception	NotFoundException
     */
	public static League addPlayers( 	LeaguePrimaryKey parentKey, 
									PlayerPrimaryKey childKey, 
									Context context )
	throws SaveException, NotFoundException
	{
		League league 	= getLeague( parentKey, context );

		// find the Player
		Player child = PlayerAWSLambdaDelegate.getPlayer( childKey, context );
		
		// add it to the Players 
		league.getPlayers().add( child );				
		
		// save the League
		league = LeagueAWSLambdaDelegate.saveLeague( league, context );

		return ( league );
	}

    /**
     * Saves multiple Player entities as the Players to the relevant League
     * @param		parentKey	LeaguePrimaryKey
     * @param		List<PlayerPrimaryKey> childKeys
     * @return    	League
     * @exception	SaveException
     * @exception	NotFoundException
     */
	public League assignPlayers( LeaguePrimaryKey parentKey, 
											List<PlayerPrimaryKey> childKeys, 
											Context context )
		throws SaveException, NotFoundException {

		League league 	= getLeague( parentKey, context );
		
		// clear out the Players 
		league.getPlayers().clear();
		
		// finally, find each child and add
		if ( childKeys != null )
		{
			Player child = null;
			for( PlayerPrimaryKey childKey : childKeys )
			{
				// retrieve the Player
				child = PlayerAWSLambdaDelegate.getPlayer( childKey, context );

				// add it to the Players List
				league.getPlayers().add( child );
			}
		}
		
		// save the League
		league = LeagueAWSLambdaDelegate.saveLeague( league, context );

		return( league );
	}

    /**
     * Delete multiple Player entities as the Players to the relevant League
     * @param		parentKey	LeaguePrimaryKey
     * @param		List<PlayerPrimaryKey> childKeys
     * @return    	League
     * @exception	DeletionException
     * @exception	NotFoundException
     * @exception	SaveException
     */
	public League deletePlayers( LeaguePrimaryKey parentKey, 
											List<PlayerPrimaryKey> childKeys, 
											Context context )
		throws DeletionException, NotFoundException, SaveException {		
		League league 	= getLeague( parentKey, context );

		if ( childKeys != null )
		{
			Set<Player> children	= league.getPlayers();
			Player child 			= null;
			
			for( PlayerPrimaryKey childKey : childKeys )
			{
				try
				{
					// first remove the relevant child from the list
					child = PlayerAWSLambdaDelegate.getPlayer( childKey, context );
					children.remove( child );
					
					// then safe to delete the child				
					PlayerAWSLambdaDelegate.deletePlayer( childKey, context );
				}
				catch( Exception exc )
				{
					String errMsg = "Deletion failed - " + exc.getMessage();
					context.getLogger().log( errMsg );
					throw new DeletionException( errMsg );
				}
			}
			
			// assign the modified list of Player back to the league
			league.setPlayers( children );			
			// save it 
			league = LeagueAWSLambdaDelegate.saveLeague( league, context );
		}
		
		return ( league );
	}

 	
    /**
     * Returns the League specific DAO.
     *
     * @return      League DAO
     */
    public static LeagueDAO getLeagueDAO()
    {
        return( new com.freeport.dao.LeagueDAO() ); 
    }

    /**
     * Release the LeagueDAO back to the FrameworkDAOFactory
     */
    public static void releaseLeagueDAO( com.freeport.dao.LeagueDAO dao )
    {
        dao = null;
    }
    
//************************************************************************
// Attributes
//************************************************************************

//    private static final Logger LOGGER = Logger.getLogger(LeagueAWSLambdaDelegate.class.getName());
}

