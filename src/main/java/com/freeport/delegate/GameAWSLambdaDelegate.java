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
 * Game AWS Lambda Proxy delegate class.
 * <p>
 * This class implements the Business Delegate design pattern for the purpose of:
 * <ol>
 * <li>Reducing coupling between the business tier and a client of the business tier by hiding all business-tier implementation details</li>
 * <li>Improving the available of Game related services in the case of a Game business related service failing.</li>
 * <li>Exposes a simpler, uniform Game interface to the business tier, making it easy for clients to consume a simple Java object.</li>
 * <li>Hides the communication protocol that may be required to fulfill Game business related services.</li>
 * </ol>
 * <p>
 * @author Dev Team
 */
@Api(value = "Game", description = "RESTful API to interact with Game resources.")
@Path("/Game")
public class GameAWSLambdaDelegate 
extends BaseAWSLambdaDelegate
{
//************************************************************************
// Public Methods
//************************************************************************
    /** 
     * Default Constructor 
     */
    public GameAWSLambdaDelegate() {
	}


    /**
     * Creates the provided Game
     * @param		businessObject 	Game
	 * @param		context		Context	
     * @return     	Game
     * @exception   CreationException
     */
    @ApiOperation(value = "Creates a Game", notes = "Creates Game using the provided data" )
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    public static Game createGame( 
    		@ApiParam(value = "Game entity to create", required = true) Game businessObject, 
    		Context context ) 
    	throws CreationException {
    	
		if ( businessObject == null )
        {
            String errMsg = "Null Game provided but not allowed " + getContextDetails(context);
            context.getLogger().log( errMsg );
            throw new CreationException( errMsg ); 
        }
      
        // return value once persisted
        GameDAO dao  = getGameDAO();
        
        try
        {
            businessObject = dao.createGame( businessObject );
        }
        catch (Exception exc)
        {
        	String errMsg = "GameAWSLambdaDelegate:createGame() - Unable to create Game" + getContextDetails(context) + exc;
        	context.getLogger().log( errMsg );
            throw new CreationException( errMsg );
        }
        finally
        {
            releaseGameDAO( dao );            
        }        
         
        return( businessObject );
         
     }

    /**
     * Method to retrieve the Game via a supplied GamePrimaryKey.
     * @param 	key
	 * @param	context		Context
     * @return 	Game
     * @exception NotFoundException - Thrown if processing any related problems
     */
    @ApiOperation(value = "Gets a Game", notes = "Gets the Game associated with the provided primary key", response = Game.class)
    @GET
    @Path("/find")
    @Produces(MediaType.APPLICATION_JSON)    
    public static Game getGame( 
    		@ApiParam(value = "Game primary key", required = true) GamePrimaryKey key, 
    		Context context  ) 
    	throws NotFoundException {
        
        Game businessObject  	= null;                
        GameDAO dao 			= getGameDAO();
            
        try
        {
        	businessObject = dao.findGame( key );
        }
        catch( Exception exc )
        {
            String errMsg = "Unable to locate Game with key " + key.toString() + " - " + getContextDetails(context) + exc;
            context.getLogger().log( errMsg );
            throw new NotFoundException( errMsg );
        }
        finally
        {
            releaseGameDAO( dao );
        }        
        
        return businessObject;
    }
     
   /**
    * Saves the provided Game
    * @param		businessObject		Game
	* @param		context		Context	
    * @return       what was just saved
    * @exception    SaveException
    */
    @ApiOperation(value = "Saves a Game", notes = "Saves Game using the provided data" )
    @PUT
    @Path("/save")
    @Consumes(MediaType.APPLICATION_JSON)
    public static Game saveGame( 
    		@ApiParam(value = "Game entity to save", required = true) Game businessObject, Context context  ) 
    	throws SaveException {

    	if ( businessObject == null )
        {
            String errMsg = "Null Game provided but not allowed " + getContextDetails(context);
            context.getLogger().log( errMsg );
            throw new SaveException( errMsg ); 
        }
    	
        // --------------------------------
        // If the businessObject has a key, find it and apply the businessObject
        // --------------------------------
        GamePrimaryKey key = businessObject.getGamePrimaryKey();
                    
        if ( key != null )
        {
            GameDAO dao = getGameDAO();

            try
            {                    
                businessObject = (Game)dao.saveGame( businessObject );
            }
            catch (Exception exc)
            {
                String errMsg = "Unable to save Game" + getContextDetails(context) + exc;
                context.getLogger().log( errMsg );
                throw new SaveException( errMsg );
            }
            finally
            {
            	releaseGameDAO( dao );
            }
        }
        else
        {
            String errMsg = "Unable to create Game due to it having a null GamePrimaryKey."; 
            context.getLogger().log( errMsg );
            throw new SaveException( errMsg );
        }
		        
        return( businessObject );
        
    }
     

	/**
     * Method to retrieve a collection of all Games
     * @param		context		Context
     * @return 	ArrayList<Game> 
     */
    @ApiOperation(value = "Get all Game", notes = "Get all Game from storage", responseContainer = "ArrayList", response = Game.class)
    @GET
    @Path("/getAll")
    @Produces(MediaType.APPLICATION_JSON)    
    public static ArrayList<Game> getAllGame( Context context ) 
    	throws NotFoundException {

        ArrayList<Game> array	= null;
        GameDAO dao 			= getGameDAO();
        
        try
        {
            array = dao.findAllGame();
        }
        catch( Exception exc )
        {
            String errMsg = "failed to getAllGame - " + getContextDetails(context) + exc.getMessage();
            context.getLogger().log( errMsg );
            throw new NotFoundException( errMsg );
        }
        finally
        {
        	releaseGameDAO( dao );
        }        
        
        return array;
    }
           
     
    /**
     * Deletes the associated business object using the provided primary key.
     * @param		key 	GamePrimaryKey
     * @param		context		Context    
     * @exception 	DeletionException
     */
    @ApiOperation(value = "Deletes a Game", notes = "Deletes the Game associated with the provided primary key", response = Game.class)
    @DELETE
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_JSON)    
	public static void deleteGame( 
			@ApiParam(value = "Game primary key", required = true) GamePrimaryKey key, 
			Context context  ) 
    	throws DeletionException {    	

    	if ( key == null )
        {
            String errMsg = "Null key provided but not allowed " + getContextDetails(context) ;
            context.getLogger().log( errMsg );
            throw new DeletionException( errMsg );
        }

        GameDAO dao  = getGameDAO();

		boolean deleted = false;
		
        try
        {                    
            deleted = dao.deleteGame( key );
        }
        catch (Exception exc)
        {
        	String errMsg = "Unable to delete Game using key = "  + key + ". " + getContextDetails(context) + exc;
        	context.getLogger().log( errMsg );
            throw new DeletionException( errMsg );
        }
        finally
        {
            releaseGameDAO( dao );
        }
         		
        return;
     }

// role related methods

    /**
     * Gets the Player using the provided primary key of a Game
     * @param		parentKey	GamePrimaryKey
     * @return    	Player
     * @exception	NotFoundException
     */
	public static Player getPlayer( GamePrimaryKey parentKey, Context context )
		throws NotFoundException {
		
		Game game 	= getGame( parentKey, context );
		PlayerPrimaryKey childKey = game.getPlayer().getPlayerPrimaryKey(); 
		Player child 				= PlayerAWSLambdaDelegate.getPlayer( childKey, context );
		
		return( child );
	}

    /**
     * Assigns the Player on a Game using the provided primary key of a Player
     * @param		parentKey	GamePrimaryKey
     * @param		parentKey	GamePrimaryKey
     * @param		context		Context
     * @return    	Game
     * @exception	SaveException
     * @exception	NotFoundException
     */
	public static Game savePlayer( GamePrimaryKey parentKey, 
												PlayerPrimaryKey childKey,
												Context context)
		throws SaveException, NotFoundException {
		
		Game game 	= getGame( parentKey, context );
		Player child 				= PlayerAWSLambdaDelegate.getPlayer( childKey, context );

		// assign the Player
		game.setPlayer( child );
	
		// save the Game 
		game = GameAWSLambdaDelegate.saveGame( game, context );
		
		return( game );
	}

    /**
     * Unassigns the Player on a Game
     * @param		parentKey	GamePrimaryKey
     * @param		Context		context
     * @return    	Game
     * @exception	SaveException
     * @exception	NotFoundException
	 * @exception	SaveException	
     */
	public static Game deletePlayer( GamePrimaryKey parentKey, Context context )
	throws DeletionException, NotFoundException, SaveException {

		Game game 	= getGame( parentKey, context );
		
		if ( game.getPlayer() != null )
		{
			PlayerPrimaryKey pk = game.getPlayer().getPlayerPrimaryKey();
			
			// first null out the Player on the parent so there's no constraint during deletion
			game.setPlayer( null );
			GameAWSLambdaDelegate.saveGame( game, context );
			
			// now it is safe to delete the Player 
			PlayerAWSLambdaDelegate.deletePlayer( pk, context );
		}

		return( game );
	}
		
 
 	
    /**
     * Returns the Game specific DAO.
     *
     * @return      Game DAO
     */
    public static GameDAO getGameDAO()
    {
        return( new com.freeport.dao.GameDAO() ); 
    }

    /**
     * Release the GameDAO back to the FrameworkDAOFactory
     */
    public static void releaseGameDAO( com.freeport.dao.GameDAO dao )
    {
        dao = null;
    }
    
//************************************************************************
// Attributes
//************************************************************************

//    private static final Logger LOGGER = Logger.getLogger(GameAWSLambdaDelegate.class.getName());
}

