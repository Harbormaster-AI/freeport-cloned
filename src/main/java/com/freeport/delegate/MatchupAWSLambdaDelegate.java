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
 * Matchup AWS Lambda Proxy delegate class.
 * <p>
 * This class implements the Business Delegate design pattern for the purpose of:
 * <ol>
 * <li>Reducing coupling between the business tier and a client of the business tier by hiding all business-tier implementation details</li>
 * <li>Improving the available of Matchup related services in the case of a Matchup business related service failing.</li>
 * <li>Exposes a simpler, uniform Matchup interface to the business tier, making it easy for clients to consume a simple Java object.</li>
 * <li>Hides the communication protocol that may be required to fulfill Matchup business related services.</li>
 * </ol>
 * <p>
 * @author Dev Team
 */
@Api(value = "Matchup", description = "RESTful API to interact with Matchup resources.")
@Path("/Matchup")
public class MatchupAWSLambdaDelegate 
extends BaseAWSLambdaDelegate
{
//************************************************************************
// Public Methods
//************************************************************************
    /** 
     * Default Constructor 
     */
    public MatchupAWSLambdaDelegate() {
	}


    /**
     * Creates the provided Matchup
     * @param		businessObject 	Matchup
	 * @param		context		Context	
     * @return     	Matchup
     * @exception   CreationException
     */
    @ApiOperation(value = "Creates a Matchup", notes = "Creates Matchup using the provided data" )
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    public static Matchup createMatchup( 
    		@ApiParam(value = "Matchup entity to create", required = true) Matchup businessObject, 
    		Context context ) 
    	throws CreationException {
    	
		if ( businessObject == null )
        {
            String errMsg = "Null Matchup provided but not allowed " + getContextDetails(context);
            context.getLogger().log( errMsg );
            throw new CreationException( errMsg ); 
        }
      
        // return value once persisted
        MatchupDAO dao  = getMatchupDAO();
        
        try
        {
            businessObject = dao.createMatchup( businessObject );
        }
        catch (Exception exc)
        {
        	String errMsg = "MatchupAWSLambdaDelegate:createMatchup() - Unable to create Matchup" + getContextDetails(context) + exc;
        	context.getLogger().log( errMsg );
            throw new CreationException( errMsg );
        }
        finally
        {
            releaseMatchupDAO( dao );            
        }        
         
        return( businessObject );
         
     }

    /**
     * Method to retrieve the Matchup via a supplied MatchupPrimaryKey.
     * @param 	key
	 * @param	context		Context
     * @return 	Matchup
     * @exception NotFoundException - Thrown if processing any related problems
     */
    @ApiOperation(value = "Gets a Matchup", notes = "Gets the Matchup associated with the provided primary key", response = Matchup.class)
    @GET
    @Path("/find")
    @Produces(MediaType.APPLICATION_JSON)    
    public static Matchup getMatchup( 
    		@ApiParam(value = "Matchup primary key", required = true) MatchupPrimaryKey key, 
    		Context context  ) 
    	throws NotFoundException {
        
        Matchup businessObject  	= null;                
        MatchupDAO dao 			= getMatchupDAO();
            
        try
        {
        	businessObject = dao.findMatchup( key );
        }
        catch( Exception exc )
        {
            String errMsg = "Unable to locate Matchup with key " + key.toString() + " - " + getContextDetails(context) + exc;
            context.getLogger().log( errMsg );
            throw new NotFoundException( errMsg );
        }
        finally
        {
            releaseMatchupDAO( dao );
        }        
        
        return businessObject;
    }
     
   /**
    * Saves the provided Matchup
    * @param		businessObject		Matchup
	* @param		context		Context	
    * @return       what was just saved
    * @exception    SaveException
    */
    @ApiOperation(value = "Saves a Matchup", notes = "Saves Matchup using the provided data" )
    @PUT
    @Path("/save")
    @Consumes(MediaType.APPLICATION_JSON)
    public static Matchup saveMatchup( 
    		@ApiParam(value = "Matchup entity to save", required = true) Matchup businessObject, Context context  ) 
    	throws SaveException {

    	if ( businessObject == null )
        {
            String errMsg = "Null Matchup provided but not allowed " + getContextDetails(context);
            context.getLogger().log( errMsg );
            throw new SaveException( errMsg ); 
        }
    	
        // --------------------------------
        // If the businessObject has a key, find it and apply the businessObject
        // --------------------------------
        MatchupPrimaryKey key = businessObject.getMatchupPrimaryKey();
                    
        if ( key != null )
        {
            MatchupDAO dao = getMatchupDAO();

            try
            {                    
                businessObject = (Matchup)dao.saveMatchup( businessObject );
            }
            catch (Exception exc)
            {
                String errMsg = "Unable to save Matchup" + getContextDetails(context) + exc;
                context.getLogger().log( errMsg );
                throw new SaveException( errMsg );
            }
            finally
            {
            	releaseMatchupDAO( dao );
            }
        }
        else
        {
            String errMsg = "Unable to create Matchup due to it having a null MatchupPrimaryKey."; 
            context.getLogger().log( errMsg );
            throw new SaveException( errMsg );
        }
		        
        return( businessObject );
        
    }
     

	/**
     * Method to retrieve a collection of all Matchups
     * @param		context		Context
     * @return 	ArrayList<Matchup> 
     */
    @ApiOperation(value = "Get all Matchup", notes = "Get all Matchup from storage", responseContainer = "ArrayList", response = Matchup.class)
    @GET
    @Path("/getAll")
    @Produces(MediaType.APPLICATION_JSON)    
    public static ArrayList<Matchup> getAllMatchup( Context context ) 
    	throws NotFoundException {

        ArrayList<Matchup> array	= null;
        MatchupDAO dao 			= getMatchupDAO();
        
        try
        {
            array = dao.findAllMatchup();
        }
        catch( Exception exc )
        {
            String errMsg = "failed to getAllMatchup - " + getContextDetails(context) + exc.getMessage();
            context.getLogger().log( errMsg );
            throw new NotFoundException( errMsg );
        }
        finally
        {
        	releaseMatchupDAO( dao );
        }        
        
        return array;
    }
           
     
    /**
     * Deletes the associated business object using the provided primary key.
     * @param		key 	MatchupPrimaryKey
     * @param		context		Context    
     * @exception 	DeletionException
     */
    @ApiOperation(value = "Deletes a Matchup", notes = "Deletes the Matchup associated with the provided primary key", response = Matchup.class)
    @DELETE
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_JSON)    
	public static void deleteMatchup( 
			@ApiParam(value = "Matchup primary key", required = true) MatchupPrimaryKey key, 
			Context context  ) 
    	throws DeletionException {    	

    	if ( key == null )
        {
            String errMsg = "Null key provided but not allowed " + getContextDetails(context) ;
            context.getLogger().log( errMsg );
            throw new DeletionException( errMsg );
        }

        MatchupDAO dao  = getMatchupDAO();

		boolean deleted = false;
		
        try
        {                    
            deleted = dao.deleteMatchup( key );
        }
        catch (Exception exc)
        {
        	String errMsg = "Unable to delete Matchup using key = "  + key + ". " + getContextDetails(context) + exc;
        	context.getLogger().log( errMsg );
            throw new DeletionException( errMsg );
        }
        finally
        {
            releaseMatchupDAO( dao );
        }
         		
        return;
     }

// role related methods

 
    /**
     * Retrieves the Games on a Matchup
     * @param		parentKey	MatchupPrimaryKey
     * @param		context		Context
     * @return    	Set<Game>
     * @exception	NotFoundException
     */
	public static Set<Game> getGames( MatchupPrimaryKey parentKey, Context context )
		throws NotFoundException
    {
    	Matchup matchup 	= getMatchup( parentKey, context );
    	return (matchup.getGames());
    }
    
    /**
     * Add the assigned Game into the Games of the relevant Matchup
     * @param		parentKey	MatchupPrimaryKey
     * @param		childKey	GamePrimaryKey
	 * @param		context		Context
     * @return    	Matchup
     * @exception	NotFoundException
     */
	public static Matchup addGames( 	MatchupPrimaryKey parentKey, 
									GamePrimaryKey childKey, 
									Context context )
	throws SaveException, NotFoundException
	{
		Matchup matchup 	= getMatchup( parentKey, context );

		// find the Game
		Game child = GameAWSLambdaDelegate.getGame( childKey, context );
		
		// add it to the Games 
		matchup.getGames().add( child );				
		
		// save the Matchup
		matchup = MatchupAWSLambdaDelegate.saveMatchup( matchup, context );

		return ( matchup );
	}

    /**
     * Saves multiple Game entities as the Games to the relevant Matchup
     * @param		parentKey	MatchupPrimaryKey
     * @param		List<GamePrimaryKey> childKeys
     * @return    	Matchup
     * @exception	SaveException
     * @exception	NotFoundException
     */
	public Matchup assignGames( MatchupPrimaryKey parentKey, 
											List<GamePrimaryKey> childKeys, 
											Context context )
		throws SaveException, NotFoundException {

		Matchup matchup 	= getMatchup( parentKey, context );
		
		// clear out the Games 
		matchup.getGames().clear();
		
		// finally, find each child and add
		if ( childKeys != null )
		{
			Game child = null;
			for( GamePrimaryKey childKey : childKeys )
			{
				// retrieve the Game
				child = GameAWSLambdaDelegate.getGame( childKey, context );

				// add it to the Games List
				matchup.getGames().add( child );
			}
		}
		
		// save the Matchup
		matchup = MatchupAWSLambdaDelegate.saveMatchup( matchup, context );

		return( matchup );
	}

    /**
     * Delete multiple Game entities as the Games to the relevant Matchup
     * @param		parentKey	MatchupPrimaryKey
     * @param		List<GamePrimaryKey> childKeys
     * @return    	Matchup
     * @exception	DeletionException
     * @exception	NotFoundException
     * @exception	SaveException
     */
	public Matchup deleteGames( MatchupPrimaryKey parentKey, 
											List<GamePrimaryKey> childKeys, 
											Context context )
		throws DeletionException, NotFoundException, SaveException {		
		Matchup matchup 	= getMatchup( parentKey, context );

		if ( childKeys != null )
		{
			Set<Game> children	= matchup.getGames();
			Game child 			= null;
			
			for( GamePrimaryKey childKey : childKeys )
			{
				try
				{
					// first remove the relevant child from the list
					child = GameAWSLambdaDelegate.getGame( childKey, context );
					children.remove( child );
					
					// then safe to delete the child				
					GameAWSLambdaDelegate.deleteGame( childKey, context );
				}
				catch( Exception exc )
				{
					String errMsg = "Deletion failed - " + exc.getMessage();
					context.getLogger().log( errMsg );
					throw new DeletionException( errMsg );
				}
			}
			
			// assign the modified list of Game back to the matchup
			matchup.setGames( children );			
			// save it 
			matchup = MatchupAWSLambdaDelegate.saveMatchup( matchup, context );
		}
		
		return ( matchup );
	}

 	
    /**
     * Returns the Matchup specific DAO.
     *
     * @return      Matchup DAO
     */
    public static MatchupDAO getMatchupDAO()
    {
        return( new com.freeport.dao.MatchupDAO() ); 
    }

    /**
     * Release the MatchupDAO back to the FrameworkDAOFactory
     */
    public static void releaseMatchupDAO( com.freeport.dao.MatchupDAO dao )
    {
        dao = null;
    }
    
//************************************************************************
// Attributes
//************************************************************************

//    private static final Logger LOGGER = Logger.getLogger(MatchupAWSLambdaDelegate.class.getName());
}

