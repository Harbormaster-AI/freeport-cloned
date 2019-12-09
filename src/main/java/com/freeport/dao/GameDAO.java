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
 * Implements the MongoDB NoSQL persistence processing for business entity Game
 * using the Morphia Java Datastore.
 *
 * @author Dev Team
 */
 public class GameDAO extends BaseDAO
{
    /**
     * default constructor
     */
    public GameDAO()
    {
    }

	
//*****************************************************
// CRUD methods
//*****************************************************

    /**
     * Retrieves a Game from the persistent store, using the provided primary key. 
     * If no match is found, a null Game is returned.
     * <p>
     * @param       pk
     * @return      Game
     * @exception   ProcessingException
     */
    public Game findGame( GamePrimaryKey pk ) 
    throws ProcessingException
    {
    	Game businessObject = null;
    	
        if (pk == null)
        {
            throw new ProcessingException("GameDAO.findGame cannot have a null primary key argument");
        }
    
		Datastore dataStore = getDatastore();
        try
        {
        	businessObject = dataStore.createQuery( Game.class )
        	       						.field( "gameId" )
        	       						.equal( (Long)pk.getFirstKey() )
        	       						.get();
		}
		catch( Throwable exc )
		{
			exc.printStackTrace();
			throw new ProcessingException( "GameDAO.findGame failed for primary key " + pk + " - " + exc );		
		}		
		finally
		{
		}		    
		
        return( businessObject );
    }
    
    /**
     * Inserts a new Game into the persistent store.
     * @param       businessObject
     * @return      newly persisted Game
     * @exception   ProcessingException
     */
    public Game createGame( Game businessObject )
    throws ProcessingException
    {
    	Datastore dataStore = getDatastore();
    	
    	try
    	{
    		// let's assign out internal unique Id
    		Long id = getNextSequence( "gameId" ) ;
    		businessObject.setGameId( id );
    		
    		dataStore.save( businessObject );
    	    LOGGER.info( "---- GameDAO.createGame created a bo is " + businessObject );
    	}
		catch( Throwable exc )
		{
			exc.printStackTrace();
			throw new ProcessingException( "GameDAO.createGame - " + exc.getMessage() );
		}		
		finally
		{
		}		
		
        // return the businessObject
        return(  businessObject );
    }
    
    /**
     * Stores the provided Game to the persistent store.
     *
     * @param       businessObject
     * @return      Game	stored entity
     * @exception   ProcessingException 
     */
    public Game saveGame( Game businessObject )
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
			throw new ProcessingException( "GameDAO:saveGame - " + exc.getMessage() );
		}		
		finally
		{
		}		    

    	return( businessObject );
    }
    
    /**
    * Removes a Game from the persistent store.
    *
    * @param        pk		identity of object to remove
    * @exception    ProcessingException
    */
    public boolean deleteGame( GamePrimaryKey pk ) 
    throws ProcessingException 
    {
    	Datastore dataStore = getDatastore();
    	
    	try
    	{
    		dataStore.delete( findGame( pk ) );
		}
		catch( Throwable exc )
		{
			LOGGER.severe("GameDAO:delete() - failed " + exc.getMessage() );
			
			exc.printStackTrace();			
			throw new ProcessingException( "GameDAO.deleteGame failed - " + exc );					
		}		
		finally
		{
		}
    	
    	return( true );
    }

    /**
     * returns a Collection of all Games
     * @return		ArrayList<Game>
     * @exception   ProcessingException
     */
    public ArrayList<Game> findAllGame()
    throws ProcessingException
    {
		final ArrayList<Game> list 	= new ArrayList<Game>();
		Datastore dataStore 				= getDatastore();
		
		try
		{
			list.addAll( dataStore.createQuery( Game.class ).asList() );
			
		}
		catch( Throwable exc )
		{
			exc.printStackTrace();
			LOGGER.warning( "GameDAO.findAll() errors - " + exc.getMessage() );
			throw new ProcessingException( "GameDAO.findAllGame failed - " + exc );		
		}		
		finally
		{
		}
		
		if ( list.size() <= 0 )
		{
			LOGGER.info( "GameDAO:findAllGames() - List is empty.");
		}
        
		return( list );		        
    }		

// abstracts and overloads from BaseDAO

	@Override
	protected String getCollectionName()
	{
		return( "com.freeport.Game" );
	}

	@Override
	protected MongoCollection<Document> createCounterCollection( MongoCollection<Document> collection )
	{
		if ( collection != null ) 
		{
		    Document document = new Document();
		    Long initialValue = new Long(0);
		    
		    document.append( MONGO_ID_FIELD_NAME, "gameId" );
		    document.append( MONGO_SEQ_FIELD_NAME, initialValue );
    	    collection.insertOne(document);
		}
		
		return( collection );
	}


//*****************************************************
// Attributes
//*****************************************************
	private static final Logger LOGGER = Logger.getLogger(Game.class.getName());
}
