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
 * Player AWS Lambda Proxy delegate class.
 * <p>
 * This class implements the Business Delegate design pattern for the purpose of:
 * <ol>
 * <li>Reducing coupling between the business tier and a client of the business tier by hiding all business-tier implementation details</li>
 * <li>Improving the available of Player related services in the case of a Player business related service failing.</li>
 * <li>Exposes a simpler, uniform Player interface to the business tier, making it easy for clients to consume a simple Java object.</li>
 * <li>Hides the communication protocol that may be required to fulfill Player business related services.</li>
 * </ol>
 * <p>
 * @author Dev Team
 */
@Api(value = "Player", description = "RESTful API to interact with Player resources.")
@Path("/Player")
public class PlayerAWSLambdaDelegate 
extends BaseAWSLambdaDelegate
{
//************************************************************************
// Public Methods
//************************************************************************
    /** 
     * Default Constructor 
     */
    public PlayerAWSLambdaDelegate() {
	}


    /**
     * Creates the provided Player
     * @param		businessObject 	Player
	 * @param		context		Context	
     * @return     	Player
     * @exception   CreationException
     */
    @ApiOperation(value = "Creates a Player", notes = "Creates Player using the provided data" )
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    public static Player createPlayer( 
    		@ApiParam(value = "Player entity to create", required = true) Player businessObject, 
    		Context context ) 
    	throws CreationException {
    	
		if ( businessObject == null )
        {
            String errMsg = "Null Player provided but not allowed " + getContextDetails(context);
            context.getLogger().log( errMsg );
            throw new CreationException( errMsg ); 
        }
      
        // return value once persisted
        PlayerDAO dao  = getPlayerDAO();
        
        try
        {
            businessObject = dao.createPlayer( businessObject );
        }
        catch (Exception exc)
        {
        	String errMsg = "PlayerAWSLambdaDelegate:createPlayer() - Unable to create Player" + getContextDetails(context) + exc;
        	context.getLogger().log( errMsg );
            throw new CreationException( errMsg );
        }
        finally
        {
            releasePlayerDAO( dao );            
        }        
         
        return( businessObject );
         
     }

    /**
     * Method to retrieve the Player via a supplied PlayerPrimaryKey.
     * @param 	key
	 * @param	context		Context
     * @return 	Player
     * @exception NotFoundException - Thrown if processing any related problems
     */
    @ApiOperation(value = "Gets a Player", notes = "Gets the Player associated with the provided primary key", response = Player.class)
    @GET
    @Path("/find")
    @Produces(MediaType.APPLICATION_JSON)    
    public static Player getPlayer( 
    		@ApiParam(value = "Player primary key", required = true) PlayerPrimaryKey key, 
    		Context context  ) 
    	throws NotFoundException {
        
        Player businessObject  	= null;                
        PlayerDAO dao 			= getPlayerDAO();
            
        try
        {
        	businessObject = dao.findPlayer( key );
        }
        catch( Exception exc )
        {
            String errMsg = "Unable to locate Player with key " + key.toString() + " - " + getContextDetails(context) + exc;
            context.getLogger().log( errMsg );
            throw new NotFoundException( errMsg );
        }
        finally
        {
            releasePlayerDAO( dao );
        }        
        
        return businessObject;
    }
     
   /**
    * Saves the provided Player
    * @param		businessObject		Player
	* @param		context		Context	
    * @return       what was just saved
    * @exception    SaveException
    */
    @ApiOperation(value = "Saves a Player", notes = "Saves Player using the provided data" )
    @PUT
    @Path("/save")
    @Consumes(MediaType.APPLICATION_JSON)
    public static Player savePlayer( 
    		@ApiParam(value = "Player entity to save", required = true) Player businessObject, Context context  ) 
    	throws SaveException {

    	if ( businessObject == null )
        {
            String errMsg = "Null Player provided but not allowed " + getContextDetails(context);
            context.getLogger().log( errMsg );
            throw new SaveException( errMsg ); 
        }
    	
        // --------------------------------
        // If the businessObject has a key, find it and apply the businessObject
        // --------------------------------
        PlayerPrimaryKey key = businessObject.getPlayerPrimaryKey();
                    
        if ( key != null )
        {
            PlayerDAO dao = getPlayerDAO();

            try
            {                    
                businessObject = (Player)dao.savePlayer( businessObject );
            }
            catch (Exception exc)
            {
                String errMsg = "Unable to save Player" + getContextDetails(context) + exc;
                context.getLogger().log( errMsg );
                throw new SaveException( errMsg );
            }
            finally
            {
            	releasePlayerDAO( dao );
            }
        }
        else
        {
            String errMsg = "Unable to create Player due to it having a null PlayerPrimaryKey."; 
            context.getLogger().log( errMsg );
            throw new SaveException( errMsg );
        }
		        
        return( businessObject );
        
    }
     

	/**
     * Method to retrieve a collection of all Players
     * @param		context		Context
     * @return 	ArrayList<Player> 
     */
    @ApiOperation(value = "Get all Player", notes = "Get all Player from storage", responseContainer = "ArrayList", response = Player.class)
    @GET
    @Path("/getAll")
    @Produces(MediaType.APPLICATION_JSON)    
    public static ArrayList<Player> getAllPlayer( Context context ) 
    	throws NotFoundException {

        ArrayList<Player> array	= null;
        PlayerDAO dao 			= getPlayerDAO();
        
        try
        {
            array = dao.findAllPlayer();
        }
        catch( Exception exc )
        {
            String errMsg = "failed to getAllPlayer - " + getContextDetails(context) + exc.getMessage();
            context.getLogger().log( errMsg );
            throw new NotFoundException( errMsg );
        }
        finally
        {
        	releasePlayerDAO( dao );
        }        
        
        return array;
    }
           
     
    /**
     * Deletes the associated business object using the provided primary key.
     * @param		key 	PlayerPrimaryKey
     * @param		context		Context    
     * @exception 	DeletionException
     */
    @ApiOperation(value = "Deletes a Player", notes = "Deletes the Player associated with the provided primary key", response = Player.class)
    @DELETE
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_JSON)    
	public static void deletePlayer( 
			@ApiParam(value = "Player primary key", required = true) PlayerPrimaryKey key, 
			Context context  ) 
    	throws DeletionException {    	

    	if ( key == null )
        {
            String errMsg = "Null key provided but not allowed " + getContextDetails(context) ;
            context.getLogger().log( errMsg );
            throw new DeletionException( errMsg );
        }

        PlayerDAO dao  = getPlayerDAO();

		boolean deleted = false;
		
        try
        {                    
            deleted = dao.deletePlayer( key );
        }
        catch (Exception exc)
        {
        	String errMsg = "Unable to delete Player using key = "  + key + ". " + getContextDetails(context) + exc;
        	context.getLogger().log( errMsg );
            throw new DeletionException( errMsg );
        }
        finally
        {
            releasePlayerDAO( dao );
        }
         		
        return;
     }

// role related methods

 
 	
    /**
     * Returns the Player specific DAO.
     *
     * @return      Player DAO
     */
    public static PlayerDAO getPlayerDAO()
    {
        return( new com.freeport.dao.PlayerDAO() ); 
    }

    /**
     * Release the PlayerDAO back to the FrameworkDAOFactory
     */
    public static void releasePlayerDAO( com.freeport.dao.PlayerDAO dao )
    {
        dao = null;
    }
    
//************************************************************************
// Attributes
//************************************************************************

//    private static final Logger LOGGER = Logger.getLogger(PlayerAWSLambdaDelegate.class.getName());
}

