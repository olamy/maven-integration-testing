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
import java.util.Arrays;

/**
 * This is a test set for <a href="http://jira.codehaus.org/browse/MNG-4314">MNG-4314</a>.
 * 
 * @author Benjamin Bentmann
 */
public class MavenITmng4314DirectInvocationOfAggregatorTest
    extends AbstractMavenIntegrationTestCase
{

    public MavenITmng4314DirectInvocationOfAggregatorTest()
    {
        super( ALL_MAVEN_VERSIONS );
    }

    /**
     * Verify that aggregator mojos invoked from the CLI run only once, namely at the top-level project.
     */
    public void testit()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-4314" );

        Verifier verifier = new Verifier( testDir.getAbsolutePath() );
        verifier.setAutoclean( false );
        verifier.deleteDirectory( "target" );
        verifier.deleteDirectory( "consumer/target" );
        verifier.deleteDirectory( "dependency/target" );
        verifier.deleteArtifacts( "org.apache.maven.its.mng4314" );
        verifier.executeGoals( Arrays.asList( new String[] { "validate", 
            "org.apache.maven.its.plugins:maven-it-plugin-all:2.1-SNAPSHOT:aggregator-dependencies" } ) );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        verifier.assertFilePresent( "target/touch.txt" );
        verifier.assertFileNotPresent( "consumer/target/touch.txt" );
        verifier.assertFileNotPresent( "dependency/target/touch.txt" );
    }

}
