/*******************************************************************************
 * Copyright 2015 DANS - Data Archiving and Networked Services
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package nl.knaw.dans.dccd.tools.reindex;

import java.net.MalformedURLException;

import joptsimple.OptionSet;
import nl.knaw.dans.common.fedora.Fedora;
import nl.knaw.dans.common.lang.repo.DmoStore;
import nl.knaw.dans.common.lang.reposearch.ReindexerCmdLine;
import nl.knaw.dans.common.lang.search.SearchEngine;
import nl.knaw.dans.common.solr.SolrSearchEngine;
import nl.knaw.dans.dccd.search.DccdSearchBeanFactory;
import nl.knaw.dans.dccd.repository.fedora.DccdFedoraStore;

/**
 * Important note:
 * The object removed from the datastore are not removed from the index; 
 * to rebuild the index (from scratch) you need to remove the index first 
 * by deleting the "data/index" directory.of your solr core
 * 
 * @author dev
 *
 */
public class DccdReindexer extends ReindexerCmdLine
{
	/**
	 * Command line version of the reindexer tool. Currently reindexes existing datasets
	 * in the DCCD repository. 
	 * 
	 * Run with -? to see a list of all parameters.
	 *
	 * Note: It currently does not purge the entire search index, but only reindex existing 
	 * datasets. Purging of the Solr cores can be achieved by removing the data dirs 
	 * and a restart of solr.
	 *                                           
	 * Add these arguments for reindexing datasets on EOF12:
	 * -store-name=easy_test -store-url=http://eof12.dans.knaw.nl/fedora32 
	 * -store-user=fedoraAdmin -store-password=###Fill-In-fedoraAdmin-Password### 
	 * -search-engine-url=http://eof12.dans.knaw.nl/solr 
	 * -content-models=fedora-system:EDM1DATASET
	 *
	 * See application.properties or tomcat.properties for the right values.
	 */
	public static void main(String[] args)
	{
		DccdReindexer reindexerTool =  new DccdReindexer();
		reindexerTool.execute(args);
	}

	private SolrSearchEngine solr;
	
	private Fedora fedora;
	
	@Override
	protected SearchEngine getSearchEngine(String solrUrl, OptionSet options)
	{
		try
		{
			solr = new SolrSearchEngine(solrUrl, new DccdSearchBeanFactory());
			return solr;
		} 
		catch (MalformedURLException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	@Override
	protected DmoStore getStore(String storeName, String storeUrl,
			String storeUser, String storePasswd, OptionSet options)
	{
		fedora = new Fedora(storeUrl, storeUser, storePasswd);
		return new DccdFedoraStore(storeName, fedora, solr);
	}

}
