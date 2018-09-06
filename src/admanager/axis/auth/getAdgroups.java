package admanager.axis.auth;

import com.beust.jcommander.Parameter;
import com.google.api.ads.adwords.axis.factory.AdWordsServices;
import com.google.api.ads.adwords.axis.utils.v201806.SelectorBuilder;
import com.google.api.ads.adwords.axis.v201806.cm.*;
import com.google.api.ads.adwords.lib.client.AdWordsSession;
import com.google.api.ads.adwords.lib.factory.AdWordsServicesInterface;
import com.google.api.ads.adwords.lib.selectorfields.v201806.cm.AdGroupField;
import com.google.api.ads.adwords.lib.utils.examples.ArgumentNames;
import com.google.api.ads.common.lib.auth.OfflineCredentials;
import com.google.api.ads.common.lib.conf.ConfigurationLoadException;
import com.google.api.ads.common.lib.exception.OAuthException;
import com.google.api.ads.common.lib.exception.ValidationException;
import com.google.api.ads.common.lib.utils.examples.CodeSampleParams;
import com.google.api.client.auth.oauth2.Credential;

import java.rmi.RemoteException;

import static com.google.api.ads.common.lib.utils.Builder.DEFAULT_CONFIGURATION_FILENAME;

public class getAdgroups {
    private static final int PAGE_SIZE = 100;

    private static class GetAdGroupsParams extends CodeSampleParams {
        @Parameter(names = ArgumentNames.CAMPAIGN_ID, required = true)
        private Long campaignId;
    }

    public static void main_get_adgroups(String[] args) {
        AdWordsSession session;
        try {
            // Generate a refreshable OAuth2 credential.
            Credential oAuth2Credential =
                    new OfflineCredentials.Builder()
                            .forApi(OfflineCredentials.Api.ADWORDS)
                            .fromFile("C:\\Users\\Ademola.A\\Downloads\\Adwords_test\\resources\\ads.properties")
                            .build()
                            .generateCredential();

            // Construct an AdWordsSession.
            session =
                    new AdWordsSession.Builder().fromFile("C:\\Users\\Ademola.A\\Downloads\\Adwords_test\\resources\\ads.properties").withOAuth2Credential(oAuth2Credential).build();
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

        AdWordsServicesInterface adWordsServices = AdWordsServices.getInstance();

        GetAdGroupsParams params = new GetAdGroupsParams();
        if (!params.parseArguments(args)) {
            // Either pass the required parameters for this example on the command line, or insert them
            // into the code here. See the parameter class definition above for descriptions.
            params.campaignId = Long.parseLong("1539353920");
        }

        try {
            runExample(adWordsServices, session, params.campaignId);
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
     * @param campaignId the ID of the campaign to use to find ad groups.
     * @throws ApiException if the API request failed with one or more service errors.
     * @throws RemoteException if the API request failed due to other errors.
     */
    public static void runExample(
            AdWordsServicesInterface adWordsServices, AdWordsSession session, Long campaignId)
            throws RemoteException {
        // Get the AdGroupService.
        AdGroupServiceInterface adGroupService =
                adWordsServices.get(session, AdGroupServiceInterface.class);

        int offset = 0;
        boolean morePages = true;

        // Create selector.
        SelectorBuilder builder = new SelectorBuilder();
        Selector selector = builder
                .fields(AdGroupField.Id, AdGroupField.Name)
                .orderAscBy(AdGroupField.Name)
                .offset(offset)
                .limit(PAGE_SIZE)
                .equals(AdGroupField.CampaignId, campaignId.toString())
                .build();

        while (morePages) {
            // Get all ad groups.
            AdGroupPage page = adGroupService.get(selector);

            // Display ad groups.
            if (page.getEntries() != null) {
                for (AdGroup adGroup : page.getEntries()) {
                    System.out.printf("Ad group with name '%s' and ID %d was found.%n", adGroup.getName(),
                            adGroup.getId());
                }
            } else {
                System.out.println("No ad groups were found.");
            }

            offset += PAGE_SIZE;
            selector = builder.increaseOffsetBy(PAGE_SIZE).build();
            morePages = offset < page.getTotalNumEntries();
        }
    }
}

