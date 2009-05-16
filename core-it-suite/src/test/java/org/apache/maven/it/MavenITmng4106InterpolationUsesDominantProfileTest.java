package org.apache.maven.it;

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

import java.io.File;
import java.util.Properties;

import org.apache.maven.it.Verifier;
import org.apache.maven.it.util.ResourceExtractor;

/**
 * This is a test set for <a href="http://jira.codehaus.org/browse/MNG-4106">MNG-4106</a>.
 * 
 * @author Benjamin Bentmann
 * @version $Id$
 */
public class MavenITmng4106InterpolationUsesDominantProfileTest
    extends AbstractMavenIntegrationTestCase
{

    public MavenITmng4106InterpolationUsesDominantProfileTest()
    {
        super( "[2.0.5,)" );
    }

    /**
     * Test that interpolation uses the property values from the dominant (i.e. last) profile among a group
     * of active profiles that define the same properties. This boils down to the proper order of profile
     * injection and interpolation, i.e. interpolate after all profiles are injected.
     */
    public void testitMNG4106()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-4106" );

        Verifier verifier = new Verifier( testDir.getAbsolutePath() );
        verifier.setAutoclean( false );
        verifier.getCliOptions().add( "--settings" );
        verifier.getCliOptions().add( "settings.xml" );
        verifier.getCliOptions().add( "-Ppom-a,pom-b,profiles-a,profiles-b,settings-a,settings-b" );
        verifier.executeGoal( "validate" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        Properties props = verifier.loadProperties( "target/pom.properties" );

        assertEquals( "b", props.getProperty( "project.properties.pomProperty" ) );
        assertEquals( "b", props.getProperty( "project.properties.pom" ) );

        assertEquals( "b", props.getProperty( "project.properties.settingsProperty" ) );
        assertEquals( "b", props.getProperty( "project.properties.settings" ) );

        if ( matchesVersionRange( "(,3.0-alpha-1)" ) )
        {
            // MNG-4060, profiles.xml support dropped
            assertEquals( "b", props.getProperty( "project.properties.profilesProperty" ) );
            assertEquals( "b", props.getProperty( "project.properties.profiles" ) );
        }
    }

}