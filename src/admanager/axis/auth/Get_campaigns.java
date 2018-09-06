// Copyright 2017 Google Inc. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package admanager.axis.auth;

import static com.google.api.ads.common.lib.utils.Builder.DEFAULT_CONFIGURATION_FILENAME;

import com.google.api.ads.adwords.axis.factory.AdWordsServices;
import com.google.api.ads.adwords.axis.utils.v201806.SelectorBuilder;
import com.google.api.ads.adwords.axis.v201806.cm.ApiError;
import com.google.api.ads.adwords.axis.v201806.cm.ApiException;
import com.google.api.ads.adwords.axis.v201806.cm.Campaign;
import com.google.api.ads.adwords.axis.v201806.cm.Budget;
import com.google.api.ads.adwords.axis.v201806.cm.CampaignPage;
import com.google.api.ads.adwords.axis.v201806.cm.CampaignServiceInterface;
import com.google.api.ads.adwords.axis.v201806.cm.Selector;
import com.google.api.ads.adwords.lib.client.AdWordsSession;
import com.google.api.ads.adwords.lib.factory.AdWordsServicesInterface;
import com.google.api.ads.adwords.lib.selectorfields.v201806.cm.CampaignField;
import com.google.api.ads.common.lib.auth.OfflineCredentials;
import com.google.api.ads.common.lib.auth.OfflineCredentials.Api;
import com.google.api.ads.common.lib.conf.ConfigurationLoadException;
import com.google.api.ads.common.lib.exception.OAuthException;
import com.google.api.ads.common.lib.exception.ValidationException;
import com.google.api.client.auth.oauth2.Credential;

import java.io.File;
import java.nio.file.Paths;
import java.rmi.RemoteException;

/**
 * This example gets all campaigns. To add a campaign, run AddCampaign.java.
 *
 * <p>Credentials and properties in {@code fromFile()} are pulled from the
 * "ads.properties" file. See README for more info.
 */
public class Get_campaigns {

    private static final int PAGE_SIZE = 100;
    public static long main_campaign_id = 0;
    public static AdWordsSession session;
    public static AdWordsServicesInterface adWordsServices;


    public static void uidasud(){


        try {
            // Generate a refreshable OAuth2 credential.
            Credential oAuth2Credential =
                    new OfflineCredentials.Builder()
                            .forApi(Api.ADWORDS)
                            .fromFile(Main_class.path)
                            .build()
                            .generateCredential();

            // Construct an AdWordsSession.

            session =
                    new AdWordsSession.Builder().fromFile(Main_class.path).withOAuth2Credential(oAuth2Credential).build();
        } catch (ConfigurationLoadException cle) {
            System.err.printf(
                    "Failed to load configuration from the %s file. Exception: %s%n",
                    DEFAULT_CONFIGURATION_FILENAME, cle);
            return;
        } catch (ValidationException ve) {
            System.err.printf(
                    "Invalid configuration in the %s file. Exception: %s%n",
                    DEFAULT_CONFIGURATION_FILENAME, ve);
            return;
        } catch (OAuthException oe) {
            System.err.printf(
                    "Failed to create OAuth credentials. Check OAuth settings in the %s file. "
                            + "Exception: %s%n",
                    DEFAULT_CONFIGURATION_FILENAME, oe);
            return;
        }

        adWordsServices = AdWordsServices.getInstance();
        try {
            runExample(adWordsServices, session);
        } catch (ApiException apiException) {
            // ApiException is the base class for most exceptions thrown by an API request. Instances
            // of this exception have a message and a collection of ApiErrors that indicate the
            // type and underlying cause of the exception. Every exception object in the adwords.axis
            // packages will return a meaningful value from toString
            //
            // ApiException extends RemoteException, so this catch block must appear before the
            // catch block for RemoteException.
            System.err.println("Request failed due to ApiException. Underlying ApiErrors:");
            if (apiException.getErrors() != null) {
                int i = 0;
                for (ApiError apiError : apiException.getErrors()) {
                    System.err.printf("  Error %d: %s%n", i++, apiError);
                }
            }
        } catch (RemoteException re) {
            System.err.printf(
                    "Request failed unexpectedly due to RemoteException: %s%n", re);
        }
    }

    /**
     * Runs the example.
     *
     * @param adWordsServices the services factory.
     * @param session the session.
     * @throws ApiException if the API request failed with one or more service errors.
     * @throws RemoteException if the API request failed due to other errors.
     */


    private static void runExample(
            AdWordsServicesInterface adWordsServices, AdWordsSession session) throws RemoteException {
        // Get the CampaignService.
        CampaignServiceInterface campaignService =
                adWordsServices.get(session, CampaignServiceInterface.class);

        int offset = 0;

        // Create selector.
        SelectorBuilder builder = new SelectorBuilder();
        Selector selector = builder
                .fields(CampaignField.Id, CampaignField.Name)
                .orderAscBy(CampaignField.Name)
                .offset(offset)
                .limit(PAGE_SIZE)
                .build();

        CampaignPage page;
        do {
            // Get all campaigns.
            page = campaignService.get(selector);

            // Display campaigns.
            if (page.getEntries() != null) {
                for (Campaign campaign : page.getEntries()) {
                    if (campaign.getName().equals("BD product names") || campaign.getName().equals("Test_ads")){
//                        System.out.printf("Campaign with name '%s' and ID %d was found.%n", campaign.getName(),
//                                campaign.getId());
//                        System.out.println(campaign.getId());
                        main_campaign_id = campaign.getId();
                        System.out.println(campaign.getName());
                    }

                }
            } else {
                System.out.println("No campaigns were found.");
            }

            offset += PAGE_SIZE;
            selector = builder.increaseOffsetBy(PAGE_SIZE).build();
        } while (offset < page.getTotalNumEntries());
    }
}
