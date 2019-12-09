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
 * Alley AWS Lambda Proxy delegate class.
 * <p>
 * This class implements the Business Delegate design pattern for the purpose of:
 * <ol>
 * <li>Reducing coupling between the business tier and a client of the business tier by hiding all business-tier implementation details</li>
 * <li>Improving the available of Alley related services in the case of a Alley business related service failing.</li>
 * <li>Exposes a simpler, uniform Alley interface to the business tier, making it easy for clients to consume a simple Java object.</li>
 * <li>Hides the communication protocol that may be required to fulfill Alley business related services.</li>
 * </ol>
 * <p>
 * @author Dev Team
 */
@Api(value = "Alley", description = "RESTful API to interact with Alley resources.")
@Path("/Alley")
public class AlleyAWSLambdaDelegate 
extends BaseAWSLambdaDelegate
{
//************************************************************************
// Public Methods
//************************************************************************
    /** 
     * Default Constructor 
     */
    public AlleyAWSLambdaDelegate() {
	}


    /**
     * Creates the provided Alley
     * @param		businessObject 	Alley
	 * @param		context		Context	
     * @return     	Alley
     * @exception   CreationException
     */
    @ApiOperation(value = "Creates a Alley", notes = "Creates Alley using the provided data" )
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    public static Alley createAlley( 
    		@ApiParam(value = "Alley entity to create", required = true) Alley businessObject, 
    		Context context ) 
    	throws CreationException {
    	
		if ( businessObject == null )
        {
            String errMsg = "Null Alley provided but not allowed " + getContextDetails(context);
            context.getLogger().log( errMsg );
            throw new CreationException( errMsg ); 
        }
      
        // return value once persisted
        AlleyDAO dao  = getAlleyDAO();
        
        try
        {
            businessObject = dao.createAlley( businessObject );
        }
        catch (Exception exc)
        {
        	String errMsg = "AlleyAWSLambdaDelegate:createAlley() - Unable to create Alley" + getContextDetails(context) + exc;
        	context.getLogger().log( errMsg );
            throw new CreationException( errMsg );
        }
        finally
        {
            releaseAlleyDAO( dao );            
        }        
         
        return( businessObject );
         
     }

    /**
     * Method to retrieve the Alley via a supplied AlleyPrimaryKey.
     * @param 	key
	 * @param	context		Context
     * @return 	Alley
     * @exception NotFoundException - Thrown if processing any related problems
     */
    @ApiOperation(value = "Gets a Alley", notes = "Gets the Alley associated with the provided primary key", response = Alley.class)
    @GET
    @Path("/find")
    @Produces(MediaType.APPLICATION_JSON)    
    public static Alley getAlley( 
    		@ApiParam(value = "Alley primary key", required = true) AlleyPrimaryKey key, 
    		Context context  ) 
    	throws NotFoundException {
        
        Alley businessObject  	= null;                
        AlleyDAO dao 			= getAlleyDAO();
            
        try
        {
        	businessObject = dao.findAlley( key );
        }
        catch( Exception exc )
        {
            String errMsg = "Unable to locate Alley with key " + key.toString() + " - " + getContextDetails(context) + exc;
            context.getLogger().log( errMsg );
            throw new NotFoundException( errMsg );
        }
        finally
        {
            releaseAlleyDAO( dao );
        }        
        
        return businessObject;
    }
     
   /**
    * Saves the provided Alley
    * @param		businessObject		Alley
	* @param		context		Context	
    * @return       what was just saved
    * @exception    SaveException
    */
    @ApiOperation(value = "Saves a Alley", notes = "Saves Alley using the provided data" )
    @PUT
    @Path("/save")
    @Consumes(MediaType.APPLICATION_JSON)
    public static Alley saveAlley( 
    		@ApiParam(value = "Alley entity to save", required = true) Alley businessObject, Context context  ) 
    	throws SaveException {

    	if ( businessObject == null )
        {
            String errMsg = "Null Alley provided but not allowed " + getContextDetails(context);
            context.getLogger().log( errMsg );
            throw new SaveException( errMsg ); 
        }
    	
        // --------------------------------
        // If the businessObject has a key, find it and apply the businessObject
        // --------------------------------
        AlleyPrimaryKey key = businessObject.getAlleyPrimaryKey();
                    
        if ( key != null )
        {
            AlleyDAO dao = getAlleyDAO();

            try
            {                    
                businessObject = (Alley)dao.saveAlley( businessObject );
            }
            catch (Exception exc)
            {
                String errMsg = "Unable to save Alley" + getContextDetails(context) + exc;
                context.getLogger().log( errMsg );
                throw new SaveException( errMsg );
            }
            finally
            {
            	releaseAlleyDAO( dao );
            }
        }
        else
        {
            String errMsg = "Unable to create Alley due to it having a null AlleyPrimaryKey."; 
            context.getLogger().log( errMsg );
            throw new SaveException( errMsg );
        }
		        
        return( businessObject );
        
    }
     

	/**
     * Method to retrieve a collection of all Alleys
     * @param		context		Context
     * @return 	ArrayList<Alley> 
     */
    @ApiOperation(value = "Get all Alley", notes = "Get all Alley from storage", responseContainer = "ArrayList", response = Alley.class)
    @GET
    @Path("/getAll")
    @Produces(MediaType.APPLICATION_JSON)    
    public static ArrayList<Alley> getAllAlley( Context context ) 
    	throws NotFoundException {

        ArrayList<Alley> array	= null;
        AlleyDAO dao 			= getAlleyDAO();
        
        try
        {
            array = dao.findAllAlley();
        }
        catch( Exception exc )
        {
            String errMsg = "failed to getAllAlley - " + getContextDetails(context) + exc.getMessage();
            context.getLogger().log( errMsg );
            throw new NotFoundException( errMsg );
        }
        finally
        {
        	releaseAlleyDAO( dao );
        }        
        
        return array;
    }
           
     
    /**
     * Deletes the associated business object using the provided primary key.
     * @param		key 	AlleyPrimaryKey
     * @param		context		Context    
     * @exception 	DeletionException
     */
    @ApiOperation(value = "Deletes a Alley", notes = "Deletes the Alley associated with the provided primary key", response = Alley.class)
    @DELETE
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_JSON)    
	public static void deleteAlley( 
			@ApiParam(value = "Alley primary key", required = true) AlleyPrimaryKey key, 
			Context context  ) 
    	throws DeletionException {    	

    	if ( key == null )
        {
            String errMsg = "Null key provided but not allowed " + getContextDetails(context) ;
            context.getLogger().log( errMsg );
            throw new DeletionException( errMsg );
        }

        AlleyDAO dao  = getAlleyDAO();

		boolean deleted = false;
		
        try
        {                    
            deleted = dao.deleteAlley( key );
        }
        catch (Exception exc)
        {
        	String errMsg = "Unable to delete Alley using key = "  + key + ". " + getContextDetails(context) + exc;
        	context.getLogger().log( errMsg );
            throw new DeletionException( errMsg );
        }
        finally
        {
            releaseAlleyDAO( dao );
        }
         		
        return;
     }

// role related methods

 
    /**
     * Retrieves the Leagues on a Alley
     * @param		parentKey	AlleyPrimaryKey
     * @param		context		Context
     * @return    	Set<League>
     * @exception	NotFoundException
     */
	public static Set<League> getLeagues( AlleyPrimaryKey parentKey, Context context )
		throws NotFoundException
    {
    	Alley alley 	= getAlley( parentKey, context );
    	return (alley.getLeagues());
    }
    
    /**
     * Add the assigned League into the Leagues of the relevant Alley
     * @param		parentKey	AlleyPrimaryKey
     * @param		childKey	LeaguePrimaryKey
	 * @param		context		Context
     * @return    	Alley
     * @exception	NotFoundException
     */
	public static Alley addLeagues( 	AlleyPrimaryKey parentKey, 
									LeaguePrimaryKey childKey, 
									Context context )
	throws SaveException, NotFoundException
	{
		Alley alley 	= getAlley( parentKey, context );

		// find the League
		League child = LeagueAWSLambdaDelegate.getLeague( childKey, context );
		
		// add it to the Leagues 
		alley.getLeagues().add( child );				
		
		// save the Alley
		alley = AlleyAWSLambdaDelegate.saveAlley( alley, context );

		return ( alley );
	}

    /**
     * Saves multiple League entities as the Leagues to the relevant Alley
     * @param		parentKey	AlleyPrimaryKey
     * @param		List<LeaguePrimaryKey> childKeys
     * @return    	Alley
     * @exception	SaveException
     * @exception	NotFoundException
     */
	public Alley assignLeagues( AlleyPrimaryKey parentKey, 
											List<LeaguePrimaryKey> childKeys, 
											Context context )
		throws SaveException, NotFoundException {

		Alley alley 	= getAlley( parentKey, context );
		
		// clear out the Leagues 
		alley.getLeagues().clear();
		
		// finally, find each child and add
		if ( childKeys != null )
		{
			League child = null;
			for( LeaguePrimaryKey childKey : childKeys )
			{
				// retrieve the League
				child = LeagueAWSLambdaDelegate.getLeague( childKey, context );

				// add it to the Leagues List
				alley.getLeagues().add( child );
			}
		}
		
		// save the Alley
		alley = AlleyAWSLambdaDelegate.saveAlley( alley, context );

		return( alley );
	}

    /**
     * Delete multiple League entities as the Leagues to the relevant Alley
     * @param		parentKey	AlleyPrimaryKey
     * @param		List<LeaguePrimaryKey> childKeys
     * @return    	Alley
     * @exception	DeletionException
     * @exception	NotFoundException
     * @exception	SaveException
     */
	public Alley deleteLeagues( AlleyPrimaryKey parentKey, 
											List<LeaguePrimaryKey> childKeys, 
											Context context )
		throws DeletionException, NotFoundException, SaveException {		
		Alley alley 	= getAlley( parentKey, context );

		if ( childKeys != null )
		{
			Set<League> children	= alley.getLeagues();
			League child 			= null;
			
			for( LeaguePrimaryKey childKey : childKeys )
			{
				try
				{
					// first remove the relevant child from the list
					child = LeagueAWSLambdaDelegate.getLeague( childKey, context );
					children.remove( child );
					
					// then safe to delete the child				
					LeagueAWSLambdaDelegate.deleteLeague( childKey, context );
				}
				catch( Exception exc )
				{
					String errMsg = "Deletion failed - " + exc.getMessage();
					context.getLogger().log( errMsg );
					throw new DeletionException( errMsg );
				}
			}
			
			// assign the modified list of League back to the alley
			alley.setLeagues( children );			
			// save it 
			alley = AlleyAWSLambdaDelegate.saveAlley( alley, context );
		}
		
		return ( alley );
	}

    /**
     * Retrieves the Tournaments on a Alley
     * @param		parentKey	AlleyPrimaryKey
     * @param		context		Context
     * @return    	Set<Tournament>
     * @exception	NotFoundException
     */
	public static Set<Tournament> getTournaments( AlleyPrimaryKey parentKey, Context context )
		throws NotFoundException
    {
    	Alley alley 	= getAlley( parentKey, context );
    	return (alley.getTournaments());
    }
    
    /**
     * Add the assigned Tournament into the Tournaments of the relevant Alley
     * @param		parentKey	AlleyPrimaryKey
     * @param		childKey	TournamentPrimaryKey
	 * @param		context		Context
     * @return    	Alley
     * @exception	NotFoundException
     */
	public static Alley addTournaments( 	AlleyPrimaryKey parentKey, 
									TournamentPrimaryKey childKey, 
									Context context )
	throws SaveException, NotFoundException
	{
		Alley alley 	= getAlley( parentKey, context );

		// find the Tournament
		Tournament child = TournamentAWSLambdaDelegate.getTournament( childKey, context );
		
		// add it to the Tournaments 
		alley.getTournaments().add( child );				
		
		// save the Alley
		alley = AlleyAWSLambdaDelegate.saveAlley( alley, context );

		return ( alley );
	}

    /**
     * Saves multiple Tournament entities as the Tournaments to the relevant Alley
     * @param		parentKey	AlleyPrimaryKey
     * @param		List<TournamentPrimaryKey> childKeys
     * @return    	Alley
     * @exception	SaveException
     * @exception	NotFoundException
     */
	public Alley assignTournaments( AlleyPrimaryKey parentKey, 
											List<TournamentPrimaryKey> childKeys, 
											Context context )
		throws SaveException, NotFoundException {

		Alley alley 	= getAlley( parentKey, context );
		
		// clear out the Tournaments 
		alley.getTournaments().clear();
		
		// finally, find each child and add
		if ( childKeys != null )
		{
			Tournament child = null;
			for( TournamentPrimaryKey childKey : childKeys )
			{
				// retrieve the Tournament
				child = TournamentAWSLambdaDelegate.getTournament( childKey, context );

				// add it to the Tournaments List
				alley.getTournaments().add( child );
			}
		}
		
		// save the Alley
		alley = AlleyAWSLambdaDelegate.saveAlley( alley, context );

		return( alley );
	}

    /**
     * Delete multiple Tournament entities as the Tournaments to the relevant Alley
     * @param		parentKey	AlleyPrimaryKey
     * @param		List<TournamentPrimaryKey> childKeys
     * @return    	Alley
     * @exception	DeletionException
     * @exception	NotFoundException
     * @exception	SaveException
     */
	public Alley deleteTournaments( AlleyPrimaryKey parentKey, 
											List<TournamentPrimaryKey> childKeys, 
											Context context )
		throws DeletionException, NotFoundException, SaveException {		
		Alley alley 	= getAlley( parentKey, context );

		if ( childKeys != null )
		{
			Set<Tournament> children	= alley.getTournaments();
			Tournament child 			= null;
			
			for( TournamentPrimaryKey childKey : childKeys )
			{
				try
				{
					// first remove the relevant child from the list
					child = TournamentAWSLambdaDelegate.getTournament( childKey, context );
					children.remove( child );
					
					// then safe to delete the child				
					TournamentAWSLambdaDelegate.deleteTournament( childKey, context );
				}
				catch( Exception exc )
				{
					String errMsg = "Deletion failed - " + exc.getMessage();
					context.getLogger().log( errMsg );
					throw new DeletionException( errMsg );
				}
			}
			
			// assign the modified list of Tournament back to the alley
			alley.setTournaments( children );			
			// save it 
			alley = AlleyAWSLambdaDelegate.saveAlley( alley, context );
		}
		
		return ( alley );
	}

    /**
     * Retrieves the Lanes on a Alley
     * @param		parentKey	AlleyPrimaryKey
     * @param		context		Context
     * @return    	Set<Lane>
     * @exception	NotFoundException
     */
	public static Set<Lane> getLanes( AlleyPrimaryKey parentKey, Context context )
		throws NotFoundException
    {
    	Alley alley 	= getAlley( parentKey, context );
    	return (alley.getLanes());
    }
    
    /**
     * Add the assigned Lane into the Lanes of the relevant Alley
     * @param		parentKey	AlleyPrimaryKey
     * @param		childKey	LanePrimaryKey
	 * @param		context		Context
     * @return    	Alley
     * @exception	NotFoundException
     */
	public static Alley addLanes( 	AlleyPrimaryKey parentKey, 
									LanePrimaryKey childKey, 
									Context context )
	throws SaveException, NotFoundException
	{
		Alley alley 	= getAlley( parentKey, context );

		// find the Lane
		Lane child = LaneAWSLambdaDelegate.getLane( childKey, context );
		
		// add it to the Lanes 
		alley.getLanes().add( child );				
		
		// save the Alley
		alley = AlleyAWSLambdaDelegate.saveAlley( alley, context );

		return ( alley );
	}

    /**
     * Saves multiple Lane entities as the Lanes to the relevant Alley
     * @param		parentKey	AlleyPrimaryKey
     * @param		List<LanePrimaryKey> childKeys
     * @return    	Alley
     * @exception	SaveException
     * @exception	NotFoundException
     */
	public Alley assignLanes( AlleyPrimaryKey parentKey, 
											List<LanePrimaryKey> childKeys, 
											Context context )
		throws SaveException, NotFoundException {

		Alley alley 	= getAlley( parentKey, context );
		
		// clear out the Lanes 
		alley.getLanes().clear();
		
		// finally, find each child and add
		if ( childKeys != null )
		{
			Lane child = null;
			for( LanePrimaryKey childKey : childKeys )
			{
				// retrieve the Lane
				child = LaneAWSLambdaDelegate.getLane( childKey, context );

				// add it to the Lanes List
				alley.getLanes().add( child );
			}
		}
		
		// save the Alley
		alley = AlleyAWSLambdaDelegate.saveAlley( alley, context );

		return( alley );
	}

    /**
     * Delete multiple Lane entities as the Lanes to the relevant Alley
     * @param		parentKey	AlleyPrimaryKey
     * @param		List<LanePrimaryKey> childKeys
     * @return    	Alley
     * @exception	DeletionException
     * @exception	NotFoundException
     * @exception	SaveException
     */
	public Alley deleteLanes( AlleyPrimaryKey parentKey, 
											List<LanePrimaryKey> childKeys, 
											Context context )
		throws DeletionException, NotFoundException, SaveException {		
		Alley alley 	= getAlley( parentKey, context );

		if ( childKeys != null )
		{
			Set<Lane> children	= alley.getLanes();
			Lane child 			= null;
			
			for( LanePrimaryKey childKey : childKeys )
			{
				try
				{
					// first remove the relevant child from the list
					child = LaneAWSLambdaDelegate.getLane( childKey, context );
					children.remove( child );
					
					// then safe to delete the child				
					LaneAWSLambdaDelegate.deleteLane( childKey, context );
				}
				catch( Exception exc )
				{
					String errMsg = "Deletion failed - " + exc.getMessage();
					context.getLogger().log( errMsg );
					throw new DeletionException( errMsg );
				}
			}
			
			// assign the modified list of Lane back to the alley
			alley.setLanes( children );			
			// save it 
			alley = AlleyAWSLambdaDelegate.saveAlley( alley, context );
		}
		
		return ( alley );
	}

 	
    /**
     * Returns the Alley specific DAO.
     *
     * @return      Alley DAO
     */
    public static AlleyDAO getAlleyDAO()
    {
        return( new com.freeport.dao.AlleyDAO() ); 
    }

    /**
     * Release the AlleyDAO back to the FrameworkDAOFactory
     */
    public static void releaseAlleyDAO( com.freeport.dao.AlleyDAO dao )
    {
        dao = null;
    }
    
//************************************************************************
// Attributes
//************************************************************************

//    private static final Logger LOGGER = Logger.getLogger(AlleyAWSLambdaDelegate.class.getName());
}

