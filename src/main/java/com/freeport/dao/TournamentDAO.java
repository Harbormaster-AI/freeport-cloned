/*******************************************************************************
  Turnstone Biologics Confidential
  
  2018 Turnstone Biologics
  All Rights Reserved.
  
  This file is subject to the terms and conditions defined in
  file 'license.txt', which is part of this source code package.
   
  Contributors :
        Turnstone Biologics - General Release
 ******************************************************************************/
package com.freeport.dao;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.mongodb.morphia.Datastore;

import org.bson.Document;
import com.mongodb.client.MongoCollection;

import com.freeport.exception.*;

    import com.freeport.primarykey.*;
    import com.freeport.bo.*;
    
/** 
 * Implements the MongoDB NoSQL persistence processing for business entity Tournament
 * using the Morphia Java Datastore.
 *
 * @author Dev Team
 */
 public class TournamentDAO extends BaseDAO
{
    /**
     * default constructor
     */
    public TournamentDAO()
    {
    }

	
//*****************************************************
// CRUD methods
//*****************************************************

    /**
     * Retrieves a Tournament from the persistent store, using the provided primary key. 
     * If no match is found, a null Tournament is returned.
     * <p>
     * @param       pk
     * @return      Tournament
     * @exception   ProcessingException
     */
    public Tournament findTournament( TournamentPrimaryKey pk ) 
    throws ProcessingException
    {
    	Tournament businessObject = null;
    	
        if (pk == null)
        {
            throw new ProcessingException("TournamentDAO.findTournament cannot have a null primary key argument");
        }
    
		Datastore dataStore = getDatastore();
        try
        {
        	businessObject = dataStore.createQuery( Tournament.class )
        	       						.field( "tournamentId" )
        	       						.equal( (Long)pk.getFirstKey() )
        	       						.get();
		}
		catch( Throwable exc )
		{
			exc.printStackTrace();
			throw new ProcessingException( "TournamentDAO.findTournament failed for primary key " + pk + " - " + exc );		
		}		
		finally
		{
		}		    
		
        return( businessObject );
    }
    
    /**
     * Inserts a new Tournament into the persistent store.
     * @param       businessObject
     * @return      newly persisted Tournament
     * @exception   ProcessingException
     */
    public Tournament createTournament( Tournament businessObject )
    throws ProcessingException
    {
    	Datastore dataStore = getDatastore();
    	
    	try
    	{
    		// let's assign out internal unique Id
    		Long id = getNextSequence( "tournamentId" ) ;
    		businessObject.setTournamentId( id );
    		
    		dataStore.save( businessObject );
    	    LOGGER.info( "---- TournamentDAO.createTournament created a bo is " + businessObject );
    	}
		catch( Throwable exc )
		{
			exc.printStackTrace();
			throw new ProcessingException( "TournamentDAO.createTournament - " + exc.getMessage() );
		}		
		finally
		{
		}		
		
        // return the businessObject
        return(  businessObject );
    }
    
    /**
     * Stores the provided Tournament to the persistent store.
     *
     * @param       businessObject
     * @return      Tournament	stored entity
     * @exception   ProcessingException 
     */
    public Tournament saveTournament( Tournament businessObject )
    throws ProcessingException
    {
    	Datastore dataStore = getDatastore();    	
    	
    	try
    	{
    		dataStore.save( businessObject );
		}
		catch( Throwable exc )
		{
			exc.printStackTrace();
			throw new ProcessingException( "TournamentDAO:saveTournament - " + exc.getMessage() );
		}		
		finally
		{
		}		    

    	return( businessObject );
    }
    
    /**
    * Removes a Tournament from the persistent store.
    *
    * @param        pk		identity of object to remove
    * @exception    ProcessingException
    */
    public boolean deleteTournament( TournamentPrimaryKey pk ) 
    throws ProcessingException 
    {
    	Datastore dataStore = getDatastore();
    	
    	try
    	{
    		dataStore.delete( findTournament( pk ) );
		}
		catch( Throwable exc )
		{
			LOGGER.severe("TournamentDAO:delete() - failed " + exc.getMessage() );
			
			exc.printStackTrace();			
			throw new ProcessingException( "TournamentDAO.deleteTournament failed - " + exc );					
		}		
		finally
		{
		}
    	
    	return( true );
    }

    /**
     * returns a Collection of all Tournaments
     * @return		ArrayList<Tournament>
     * @exception   ProcessingException
     */
    public ArrayList<Tournament> findAllTournament()
    throws ProcessingException
    {
		final ArrayList<Tournament> list 	= new ArrayList<Tournament>();
		Datastore dataStore 				= getDatastore();
		
		try
		{
			list.addAll( dataStore.createQuery( Tournament.class ).asList() );
			
		}
		catch( Throwable exc )
		{
			exc.printStackTrace();
			LOGGER.warning( "TournamentDAO.findAll() errors - " + exc.getMessage() );
			throw new ProcessingException( "TournamentDAO.findAllTournament failed - " + exc );		
		}		
		finally
		{
		}
		
		if ( list.size() <= 0 )
		{
			LOGGER.info( "TournamentDAO:findAllTournaments() - List is empty.");
		}
        
		return( list );		        
    }		

// abstracts and overloads from BaseDAO

	@Override
	protected String getCollectionName()
	{
		return( "com.freeport.Tournament" );
	}

	@Override
	protected MongoCollection<Document> createCounterCollection( MongoCollection<Document> collection )
	{
		if ( collection != null ) 
		{
		    Document document = new Document();
		    Long initialValue = new Long(0);
		    
		    document.append( MONGO_ID_FIELD_NAME, "tournamentId" );
		    document.append( MONGO_SEQ_FIELD_NAME, initialValue );
    	    collection.insertOne(document);
		}
		
		return( collection );
	}


//*****************************************************
// Attributes
//*****************************************************
	private static final Logger LOGGER = Logger.getLogger(Tournament.class.getName());
}
