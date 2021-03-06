package org.apache.commons.jcs.auxiliary.remote.util;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.apache.commons.jcs.auxiliary.remote.value.RemoteCacheRequest;
import org.apache.commons.jcs.auxiliary.remote.value.RemoteRequestType;
import org.apache.commons.jcs.engine.behavior.ICacheElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Set;

/**
 * This creates request objects. You could write your own client and use the objects from this
 * factory.
 */
public class RemoteCacheRequestFactory
{
    /** The Logger. */
    private static final Log log = LogFactory.getLog( RemoteCacheRequestFactory.class );

    /**
     * Creates a get Request.
     * <p>
     * @param cacheName
     * @param key
     * @param requesterId
     * @return RemoteHttpCacheRequest
     */
    public static <K, V> RemoteCacheRequest<K, V> createGetRequest( String cacheName, K key, long requesterId )
    {
        RemoteCacheRequest<K, V> request = new RemoteCacheRequest<K, V>();
        request.setCacheName( cacheName );
        request.setKey( key );
        request.setRequesterId( requesterId );
        request.setRequestType( RemoteRequestType.GET );

        if ( log.isDebugEnabled() )
        {
            log.debug( "Created: " + request );
        }

        return request;
    }

    /**
     * Creates a getMatching Request.
     * <p>
     * @param cacheName
     * @param pattern
     * @param requesterId
     * @return RemoteHttpCacheRequest
     */
    public static <K, V> RemoteCacheRequest<K, V> createGetMatchingRequest( String cacheName, String pattern, long requesterId )
    {
        RemoteCacheRequest<K, V> request = new RemoteCacheRequest<K, V>();
        request.setCacheName( cacheName );
        request.setPattern( pattern );
        request.setRequesterId( requesterId );
        request.setRequestType( RemoteRequestType.GET_MATCHING );

        if ( log.isDebugEnabled() )
        {
            log.debug( "Created: " + request );
        }

        return request;
    }

    /**
     * Creates a getMultiple Request.
     * <p>
     * @param cacheName
     * @param keys
     * @param requesterId
     * @return RemoteHttpCacheRequest
     */
    public static <K, V> RemoteCacheRequest<K, V> createGetMultipleRequest( String cacheName, Set<K> keys, long requesterId )
    {
        RemoteCacheRequest<K, V> request = new RemoteCacheRequest<K, V>();
        request.setCacheName( cacheName );
        request.setKeySet( keys );
        request.setRequesterId( requesterId );
        request.setRequestType( RemoteRequestType.GET_MULTIPLE );

        if ( log.isDebugEnabled() )
        {
            log.debug( "Created: " + request );
        }

        return request;
    }

    /**
     * Creates a remove Request.
     * <p>
     * @param cacheName
     * @param key
     * @param requesterId
     * @return RemoteHttpCacheRequest
     */
    public static <K, V> RemoteCacheRequest<K, V> createRemoveRequest( String cacheName, K key, long requesterId )
    {
        RemoteCacheRequest<K, V> request = new RemoteCacheRequest<K, V>();
        request.setCacheName( cacheName );
        request.setKey( key );
        request.setRequesterId( requesterId );
        request.setRequestType( RemoteRequestType.REMOVE );

        if ( log.isDebugEnabled() )
        {
            log.debug( "Created: " + request );
        }

        return request;
    }

    /**
     * Creates a GetKeySet Request.
     * <p>
     * @param cacheName
     * @param requesterId
     * @return RemoteHttpCacheRequest
     */
    public static RemoteCacheRequest<String, String> createGetKeySetRequest( String cacheName, long requesterId )
    {
        RemoteCacheRequest<String, String> request = new RemoteCacheRequest<String, String>();
        request.setCacheName( cacheName );
        request.setKey( cacheName );
        request.setRequesterId( requesterId );
        request.setRequestType( RemoteRequestType.GET_KEYSET );

        if ( log.isDebugEnabled() )
        {
            log.debug( "Created: " + request );
        }

        return request;
    }

    /**
     * Creates a removeAll Request.
     * <p>
     * @param cacheName
     * @param requesterId
     * @return RemoteHttpCacheRequest
     */
    public static <K, V> RemoteCacheRequest<K, V> createRemoveAllRequest( String cacheName, long requesterId )
    {
        RemoteCacheRequest<K, V> request = new RemoteCacheRequest<K, V>();
        request.setCacheName( cacheName );
        request.setRequesterId( requesterId );
        request.setRequestType( RemoteRequestType.REMOVE_ALL );

        if ( log.isDebugEnabled() )
        {
            log.debug( "Created: " + request );
        }

        return request;
    }

    /**
     * Creates a dispose Request.
     * <p>
     * @param cacheName
     * @param requesterId
     * @return RemoteHttpCacheRequest
     */
    public static <K, V> RemoteCacheRequest<K, V> createDisposeRequest( String cacheName, long requesterId )
    {
        RemoteCacheRequest<K, V> request = new RemoteCacheRequest<K, V>();
        request.setCacheName( cacheName );
        request.setRequesterId( requesterId );
        request.setRequestType( RemoteRequestType.DISPOSE );

        if ( log.isDebugEnabled() )
        {
            log.debug( "Created: " + request );
        }

        return request;
    }

    /**
     * Creates an Update Request.
     * <p>
     * @param cacheElement
     * @param requesterId
     * @return RemoteHttpCacheRequest
     */
    public static <K, V> RemoteCacheRequest<K, V> createUpdateRequest( ICacheElement<K, V> cacheElement, long requesterId )
    {
        RemoteCacheRequest<K, V> request = new RemoteCacheRequest<K, V>();
        if ( cacheElement != null )
        {
            request.setCacheName( cacheElement.getCacheName() );
            request.setCacheElement( cacheElement );
            request.setKey( cacheElement.getKey() );
        }
        else
        {
            log.error( "Can't create a proper update request for a null cache element." );
        }
        request.setRequesterId( requesterId );
        request.setRequestType( RemoteRequestType.UPDATE );

        if ( log.isDebugEnabled() )
        {
            log.debug( "Created: " + request );
        }

        return request;
    }

    /**
     * Creates an alive check Request.
     * <p>
     * @param requesterId
     * @return RemoteHttpCacheRequest
     */
    public static <K, V> RemoteCacheRequest<K, V> createAliveCheckRequest( long requesterId )
    {
        RemoteCacheRequest<K, V> request = new RemoteCacheRequest<K, V>();
        request.setRequesterId( requesterId );
        request.setRequestType( RemoteRequestType.ALIVE_CHECK );

        if ( log.isDebugEnabled() )
        {
            log.debug( "Created: " + request );
        }

        return request;
    }


}
