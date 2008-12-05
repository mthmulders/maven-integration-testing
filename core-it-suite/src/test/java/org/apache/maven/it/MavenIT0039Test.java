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

import org.apache.maven.it.Verifier;
import org.apache.maven.it.util.ResourceExtractor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a test set for <a href="http://jira.codehaus.org/browse/MNG-607">MNG-607</a>.
 * 
 * @author John Casey
 * @version $Id$
 */
public class MavenIT0039Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test reactor for projects that have release-pom.xml in addition to
     * pom.xml. The release-pom.xml file should be chosen above pom.xml for
     * these projects in the build.
     */
    public void testit0039()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/it0039" );
        Verifier verifier = new Verifier( testDir.getAbsolutePath() );
        List cliOptions = new ArrayList();
        cliOptions.add( "-r" );
        verifier.setCliOptions( cliOptions );
        verifier.executeGoal( "package" );
        verifier.assertFilePresent( "project/target/maven-it-it0039-p1-1.0.jar" );
        verifier.assertFilePresent( "project2/target/maven-it-it0039-p2-1.0.jar" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

    }
}

