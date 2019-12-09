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
 * Lane AWS Lambda Proxy delegate class.
 * <p>
 * This class implements the Business Delegate design pattern for the purpose of:
 * <ol>
 * <li>Reducing coupling between the business tier and a client of the business tier by hiding all business-tier implementation details</li>
 * <li>Improving the available of Lane related services in the case of a Lane business related service failing.</li>
 * <li>Exposes a simpler, uniform Lane interface to the business tier, making it easy for clients to consume a simple Java object.</li>
 * <li>Hides the communication protocol that may be required to fulfill Lane business related services.</li>
 * </ol>
 * <p>
 * @author Dev Team
 */
@Api(value = "Lane", description = "RESTful API to interact with Lane resources.")
@Path("/Lane")
public class LaneAWSLambdaDelegate 
extends BaseAWSLambdaDelegate
{
//************************************************************************
// Public Methods
//************************************************************************
    /** 
     * Default Constructor 
     */
    public LaneAWSLambdaDelegate() {
	}


    /**
     * Creates the provided Lane
     * @param		businessObject 	Lane
	 * @param		context		Context	
     * @return     	Lane
     * @exception   CreationException
     */
    @ApiOperation(value = "Creates a Lane", notes = "Creates Lane using the provided data" )
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    public static Lane createLane( 
    		@ApiParam(value = "Lane entity to create", required = true) Lane businessObject, 
    		Context context ) 
    	throws CreationException {
    	
		if ( businessObject == null )
        {
            String errMsg = "Null Lane provided but not allowed " + getContextDetails(context);
            context.getLogger().log( errMsg );
            throw new CreationException( errMsg ); 
        }
      
        // return value once persisted
        LaneDAO dao  = getLaneDAO();
        
        try
        {
            businessObject = dao.createLane( businessObject );
        }
        catch (Exception exc)
        {
        	String errMsg = "LaneAWSLambdaDelegate:createLane() - Unable to create Lane" + getContextDetails(context) + exc;
        	context.getLogger().log( errMsg );
            throw new CreationException( errMsg );
        }
        finally
        {
            releaseLaneDAO( dao );            
        }        
         
        return( businessObject );
         
     }

    /**
     * Method to retrieve the Lane via a supplied LanePrimaryKey.
     * @param 	key
	 * @param	context		Context
     * @return 	Lane
     * @exception NotFoundException - Thrown if processing any related problems
     */
    @ApiOperation(value = "Gets a Lane", notes = "Gets the Lane associated with the provided primary key", response = Lane.class)
    @GET
    @Path("/find")
    @Produces(MediaType.APPLICATION_JSON)    
    public static Lane getLane( 
    		@ApiParam(value = "Lane primary key", required = true) LanePrimaryKey key, 
    		Context context  ) 
    	throws NotFoundException {
        
        Lane businessObject  	= null;                
        LaneDAO dao 			= getLaneDAO();
            
        try
        {
        	businessObject = dao.findLane( key );
        }
        catch( Exception exc )
        {
            String errMsg = "Unable to locate Lane with key " + key.toString() + " - " + getContextDetails(context) + exc;
            context.getLogger().log( errMsg );
            throw new NotFoundException( errMsg );
        }
        finally
        {
            releaseLaneDAO( dao );
        }        
        
        return businessObject;
    }
     
   /**
    * Saves the provided Lane
    * @param		businessObject		Lane
	* @param		context		Context	
    * @return       what was just saved
    * @exception    SaveException
    */
    @ApiOperation(value = "Saves a Lane", notes = "Saves Lane using the provided data" )
    @PUT
    @Path("/save")
    @Consumes(MediaType.APPLICATION_JSON)
    public static Lane saveLane( 
    		@ApiParam(value = "Lane entity to save", required = true) Lane businessObject, Context context  ) 
    	throws SaveException {

    	if ( businessObject == null )
        {
            String errMsg = "Null Lane provided but not allowed " + getContextDetails(context);
            context.getLogger().log( errMsg );
            throw new SaveException( errMsg ); 
        }
    	
        // --------------------------------
        // If the businessObject has a key, find it and apply the businessObject
        // --------------------------------
        LanePrimaryKey key = businessObject.getLanePrimaryKey();
                    
        if ( key != null )
        {
            LaneDAO dao = getLaneDAO();

            try
            {                    
                businessObject = (Lane)dao.saveLane( businessObject );
            }
            catch (Exception exc)
            {
                String errMsg = "Unable to save Lane" + getContextDetails(context) + exc;
                context.getLogger().log( errMsg );
                throw new SaveException( errMsg );
            }
            finally
            {
            	releaseLaneDAO( dao );
            }
        }
        else
        {
            String errMsg = "Unable to create Lane due to it having a null LanePrimaryKey."; 
            context.getLogger().log( errMsg );
            throw new SaveException( errMsg );
        }
		        
        return( businessObject );
        
    }
     

	/**
     * Method to retrieve a collection of all Lanes
     * @param		context		Context
     * @return 	ArrayList<Lane> 
     */
    @ApiOperation(value = "Get all Lane", notes = "Get all Lane from storage", responseContainer = "ArrayList", response = Lane.class)
    @GET
    @Path("/getAll")
    @Produces(MediaType.APPLICATION_JSON)    
    public static ArrayList<Lane> getAllLane( Context context ) 
    	throws NotFoundException {

        ArrayList<Lane> array	= null;
        LaneDAO dao 			= getLaneDAO();
        
        try
        {
            array = dao.findAllLane();
        }
        catch( Exception exc )
        {
            String errMsg = "failed to getAllLane - " + getContextDetails(context) + exc.getMessage();
            context.getLogger().log( errMsg );
            throw new NotFoundException( errMsg );
        }
        finally
        {
        	releaseLaneDAO( dao );
        }        
        
        return array;
    }
           
     
    /**
     * Deletes the associated business object using the provided primary key.
     * @param		key 	LanePrimaryKey
     * @param		context		Context    
     * @exception 	DeletionException
     */
    @ApiOperation(value = "Deletes a Lane", notes = "Deletes the Lane associated with the provided primary key", response = Lane.class)
    @DELETE
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_JSON)    
	public static void deleteLane( 
			@ApiParam(value = "Lane primary key", required = true) LanePrimaryKey key, 
			Context context  ) 
    	throws DeletionException {    	

    	if ( key == null )
        {
            String errMsg = "Null key provided but not allowed " + getContextDetails(context) ;
            context.getLogger().log( errMsg );
            throw new DeletionException( errMsg );
        }

        LaneDAO dao  = getLaneDAO();

		boolean deleted = false;
		
        try
        {                    
            deleted = dao.deleteLane( key );
        }
        catch (Exception exc)
        {
        	String errMsg = "Unable to delete Lane using key = "  + key + ". " + getContextDetails(context) + exc;
        	context.getLogger().log( errMsg );
            throw new DeletionException( errMsg );
        }
        finally
        {
            releaseLaneDAO( dao );
        }
         		
        return;
     }

// role related methods

 
 	
    /**
     * Returns the Lane specific DAO.
     *
     * @return      Lane DAO
     */
    public static LaneDAO getLaneDAO()
    {
        return( new com.freeport.dao.LaneDAO() ); 
    }

    /**
     * Release the LaneDAO back to the FrameworkDAOFactory
     */
    public static void releaseLaneDAO( com.freeport.dao.LaneDAO dao )
    {
        dao = null;
    }
    
//************************************************************************
// Attributes
//************************************************************************

//    private static final Logger LOGGER = Logger.getLogger(LaneAWSLambdaDelegate.class.getName());
}

