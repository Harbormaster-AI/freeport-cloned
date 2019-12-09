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
 * Implements the MongoDB NoSQL persistence processing for business entity Matchup
 * using the Morphia Java Datastore.
 *
 * @author Dev Team
 */
 public class MatchupDAO extends BaseDAO
{
    /**
     * default constructor
     */
    public MatchupDAO()
    {
    }

	
//*****************************************************
// CRUD methods
//*****************************************************

    /**
     * Retrieves a Matchup from the persistent store, using the provided primary key. 
     * If no match is found, a null Matchup is returned.
     * <p>
     * @param       pk
     * @return      Matchup
     * @exception   ProcessingException
     */
    public Matchup findMatchup( MatchupPrimaryKey pk ) 
    throws ProcessingException
    {
    	Matchup businessObject = null;
    	
        if (pk == null)
        {
            throw new ProcessingException("MatchupDAO.findMatchup cannot have a null primary key argument");
        }
    
		Datastore dataStore = getDatastore();
        try
        {
        	businessObject = dataStore.createQuery( Matchup.class )
        	       						.field( "matchupId" )
        	       						.equal( (Long)pk.getFirstKey() )
        	       						.get();
		}
		catch( Throwable exc )
		{
			exc.printStackTrace();
			throw new ProcessingException( "MatchupDAO.findMatchup failed for primary key " + pk + " - " + exc );		
		}		
		finally
		{
		}		    
		
        return( businessObject );
    }
    
    /**
     * Inserts a new Matchup into the persistent store.
     * @param       businessObject
     * @return      newly persisted Matchup
     * @exception   ProcessingException
     */
    public Matchup createMatchup( Matchup businessObject )
    throws ProcessingException
    {
    	Datastore dataStore = getDatastore();
    	
    	try
    	{
    		// let's assign out internal unique Id
    		Long id = getNextSequence( "matchupId" ) ;
    		businessObject.setMatchupId( id );
    		
    		dataStore.save( businessObject );
    	    LOGGER.info( "---- MatchupDAO.createMatchup created a bo is " + businessObject );
    	}
		catch( Throwable exc )
		{
			exc.printStackTrace();
			throw new ProcessingException( "MatchupDAO.createMatchup - " + exc.getMessage() );
		}		
		finally
		{
		}		
		
        // return the businessObject
        return(  businessObject );
    }
    
    /**
     * Stores the provided Matchup to the persistent store.
     *
     * @param       businessObject
     * @return      Matchup	stored entity
     * @exception   ProcessingException 
     */
    public Matchup saveMatchup( Matchup businessObject )
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
			throw new ProcessingException( "MatchupDAO:saveMatchup - " + exc.getMessage() );
		}		
		finally
		{
		}		    

    	return( businessObject );
    }
    
    /**
    * Removes a Matchup from the persistent store.
    *
    * @param        pk		identity of object to remove
    * @exception    ProcessingException
    */
    public boolean deleteMatchup( MatchupPrimaryKey pk ) 
    throws ProcessingException 
    {
    	Datastore dataStore = getDatastore();
    	
    	try
    	{
    		dataStore.delete( findMatchup( pk ) );
		}
		catch( Throwable exc )
		{
			LOGGER.severe("MatchupDAO:delete() - failed " + exc.getMessage() );
			
			exc.printStackTrace();			
			throw new ProcessingException( "MatchupDAO.deleteMatchup failed - " + exc );					
		}		
		finally
		{
		}
    	
    	return( true );
    }

    /**
     * returns a Collection of all Matchups
     * @return		ArrayList<Matchup>
     * @exception   ProcessingException
     */
    public ArrayList<Matchup> findAllMatchup()
    throws ProcessingException
    {
		final ArrayList<Matchup> list 	= new ArrayList<Matchup>();
		Datastore dataStore 				= getDatastore();
		
		try
		{
			list.addAll( dataStore.createQuery( Matchup.class ).asList() );
			
		}
		catch( Throwable exc )
		{
			exc.printStackTrace();
			LOGGER.warning( "MatchupDAO.findAll() errors - " + exc.getMessage() );
			throw new ProcessingException( "MatchupDAO.findAllMatchup failed - " + exc );		
		}		
		finally
		{
		}
		
		if ( list.size() <= 0 )
		{
			LOGGER.info( "MatchupDAO:findAllMatchups() - List is empty.");
		}
        
		return( list );		        
    }		

// abstracts and overloads from BaseDAO

	@Override
	protected String getCollectionName()
	{
		return( "com.freeport.Matchup" );
	}

	@Override
	protected MongoCollection<Document> createCounterCollection( MongoCollection<Document> collection )
	{
		if ( collection != null ) 
		{
		    Document document = new Document();
		    Long initialValue = new Long(0);
		    
		    document.append( MONGO_ID_FIELD_NAME, "matchupId" );
		    document.append( MONGO_SEQ_FIELD_NAME, initialValue );
    	    collection.insertOne(document);
		}
		
		return( collection );
	}


//*****************************************************
// Attributes
//*****************************************************
	private static final Logger LOGGER = Logger.getLogger(Matchup.class.getName());
}
