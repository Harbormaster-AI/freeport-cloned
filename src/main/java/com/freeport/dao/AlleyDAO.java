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
 * Implements the MongoDB NoSQL persistence processing for business entity Alley
 * using the Morphia Java Datastore.
 *
 * @author Dev Team
 */
 public class AlleyDAO extends BaseDAO
{
    /**
     * default constructor
     */
    public AlleyDAO()
    {
    }

	
//*****************************************************
// CRUD methods
//*****************************************************

    /**
     * Retrieves a Alley from the persistent store, using the provided primary key. 
     * If no match is found, a null Alley is returned.
     * <p>
     * @param       pk
     * @return      Alley
     * @exception   ProcessingException
     */
    public Alley findAlley( AlleyPrimaryKey pk ) 
    throws ProcessingException
    {
    	Alley businessObject = null;
    	
        if (pk == null)
        {
            throw new ProcessingException("AlleyDAO.findAlley cannot have a null primary key argument");
        }
    
		Datastore dataStore = getDatastore();
        try
        {
        	businessObject = dataStore.createQuery( Alley.class )
        	       						.field( "alleyId" )
        	       						.equal( (Long)pk.getFirstKey() )
        	       						.get();
		}
		catch( Throwable exc )
		{
			exc.printStackTrace();
			throw new ProcessingException( "AlleyDAO.findAlley failed for primary key " + pk + " - " + exc );		
		}		
		finally
		{
		}		    
		
        return( businessObject );
    }
    
    /**
     * Inserts a new Alley into the persistent store.
     * @param       businessObject
     * @return      newly persisted Alley
     * @exception   ProcessingException
     */
    public Alley createAlley( Alley businessObject )
    throws ProcessingException
    {
    	Datastore dataStore = getDatastore();
    	
    	try
    	{
    		// let's assign out internal unique Id
    		Long id = getNextSequence( "alleyId" ) ;
    		businessObject.setAlleyId( id );
    		
    		dataStore.save( businessObject );
    	    LOGGER.info( "---- AlleyDAO.createAlley created a bo is " + businessObject );
    	}
		catch( Throwable exc )
		{
			exc.printStackTrace();
			throw new ProcessingException( "AlleyDAO.createAlley - " + exc.getMessage() );
		}		
		finally
		{
		}		
		
        // return the businessObject
        return(  businessObject );
    }
    
    /**
     * Stores the provided Alley to the persistent store.
     *
     * @param       businessObject
     * @return      Alley	stored entity
     * @exception   ProcessingException 
     */
    public Alley saveAlley( Alley businessObject )
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
			throw new ProcessingException( "AlleyDAO:saveAlley - " + exc.getMessage() );
		}		
		finally
		{
		}		    

    	return( businessObject );
    }
    
    /**
    * Removes a Alley from the persistent store.
    *
    * @param        pk		identity of object to remove
    * @exception    ProcessingException
    */
    public boolean deleteAlley( AlleyPrimaryKey pk ) 
    throws ProcessingException 
    {
    	Datastore dataStore = getDatastore();
    	
    	try
    	{
    		dataStore.delete( findAlley( pk ) );
		}
		catch( Throwable exc )
		{
			LOGGER.severe("AlleyDAO:delete() - failed " + exc.getMessage() );
			
			exc.printStackTrace();			
			throw new ProcessingException( "AlleyDAO.deleteAlley failed - " + exc );					
		}		
		finally
		{
		}
    	
    	return( true );
    }

    /**
     * returns a Collection of all Alleys
     * @return		ArrayList<Alley>
     * @exception   ProcessingException
     */
    public ArrayList<Alley> findAllAlley()
    throws ProcessingException
    {
		final ArrayList<Alley> list 	= new ArrayList<Alley>();
		Datastore dataStore 				= getDatastore();
		
		try
		{
			list.addAll( dataStore.createQuery( Alley.class ).asList() );
			
		}
		catch( Throwable exc )
		{
			exc.printStackTrace();
			LOGGER.warning( "AlleyDAO.findAll() errors - " + exc.getMessage() );
			throw new ProcessingException( "AlleyDAO.findAllAlley failed - " + exc );		
		}		
		finally
		{
		}
		
		if ( list.size() <= 0 )
		{
			LOGGER.info( "AlleyDAO:findAllAlleys() - List is empty.");
		}
        
		return( list );		        
    }		

// abstracts and overloads from BaseDAO

	@Override
	protected String getCollectionName()
	{
		return( "com.freeport.Alley" );
	}

	@Override
	protected MongoCollection<Document> createCounterCollection( MongoCollection<Document> collection )
	{
		if ( collection != null ) 
		{
		    Document document = new Document();
		    Long initialValue = new Long(0);
		    
		    document.append( MONGO_ID_FIELD_NAME, "alleyId" );
		    document.append( MONGO_SEQ_FIELD_NAME, initialValue );
    	    collection.insertOne(document);
		}
		
		return( collection );
	}


//*****************************************************
// Attributes
//*****************************************************
	private static final Logger LOGGER = Logger.getLogger(Alley.class.getName());
}
