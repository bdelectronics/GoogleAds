package admanager.axis.auth;

import com.beust.jcommander.Parameter;
import com.google.api.ads.adwords.axis.v201806.cm.AdGroupAd;
import com.google.api.ads.adwords.axis.v201806.cm.AdGroupAdOperation;
import com.google.api.ads.adwords.axis.v201806.cm.AdGroupAdReturnValue;
import com.google.api.ads.adwords.axis.v201806.cm.AdGroupAdServiceInterface;
import com.google.api.ads.adwords.axis.v201806.cm.AdGroupAdStatus;
import com.google.api.ads.adwords.axis.v201806.cm.ApiError;
import com.google.api.ads.adwords.axis.v201806.cm.ApiException;
import com.google.api.ads.adwords.axis.v201806.cm.ExpandedTextAd;
import com.google.api.ads.adwords.axis.v201806.cm.Operator;
import com.google.api.ads.adwords.lib.client.AdWordsSession;
import com.google.api.ads.adwords.lib.factory.AdWordsServicesInterface;
import com.google.api.ads.adwords.lib.utils.examples.ArgumentNames;
import com.google.api.ads.common.lib.utils.examples.CodeSampleParams;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import static admanager.axis.auth.Get_campaigns.adWordsServices;
import static admanager.axis.auth.Get_campaigns.session;

public class Add_ads {


    private static class AddExpandedTextAdsParams extends CodeSampleParams {
        @Parameter(names = ArgumentNames.AD_GROUP_ID, required = true)
        private Long adGroupId;
        private String Final_url, Headline1, Headline2,
        Path1, Path2, Description1, Tracking_url_template, Final_URL_suffix, First, second;
    }

    public static void adwords_main(Long adGroupId, String Final_url, String Headline1, String Headline2,
                                    String Path1, String Path2, String Description1, String Tracking_url_template,
                                    String Final_URL_suffix, String First, String second){



        AddExpandedTextAdsParams params = new AddExpandedTextAdsParams();
            params.adGroupId = adGroupId;
            params.Description1 = Description1;
            params.Final_url = Final_url;
            params.Path1 = Path1;
            params.Path2 = Path2;
            params.Headline1 = Headline1;
            params.Headline2 = Headline2;
            params.Final_URL_suffix = Final_URL_suffix;
            params.Tracking_url_template = Tracking_url_template;
            params.First = First;
            params.second = second;


        try {
            runExample(adWordsServices, session, params.adGroupId, params.Final_url, params.Headline1, params.Headline2, params.Path1,
                    params.Path2, params.Description1, params.Tracking_url_template, params.Final_URL_suffix, params.First, params.second);
        } catch (ApiException apiException) {
            Main_class.error += 1;
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
            Main_class.error += 1;
            System.err.printf(
                    "Request failed unexpectedly due to RemoteException: %s%n", re);
        }

    }

    /**
     * Runs the example.
     *
     * @param adWordsServices the services factory.
     * @param session the session.
     * @param Final_url the final url
     * @param adGroupId the ID of the ad group where the ad will be created.
     * @throws ApiException if the API request failed with one or more service errors.
     * @throws RemoteException if the API request failed due to other errors.
     */
    public static void runExample(
            AdWordsServicesInterface adWordsServices, AdWordsSession session, long adGroupId, String Final_url, String Headline1, String Headline2,
            String Path1, String Path2, String Description1, String Tracking_url_template,
            String Final_URL_suffix, String First, String second)
            throws RemoteException {
        // Get the AdGroupAdService.
        AdGroupAdServiceInterface adGroupAdService =
                adWordsServices.get(session, AdGroupAdServiceInterface.class);

        List<AdGroupAdOperation> operations = new ArrayList<>();

            // Create expanded text ad.
            ExpandedTextAd expandedTextAd = new ExpandedTextAd();
            expandedTextAd.setFinalUrls(new String[] {Final_url});
            expandedTextAd.setHeadlinePart1(Headline1);
            expandedTextAd.setHeadlinePart2(Headline2);
            expandedTextAd.setPath1(Path1);
            expandedTextAd.setPath2(Path2);
            expandedTextAd.setDescription(Description1);
            expandedTextAd.setTrackingUrlTemplate(Tracking_url_template);
            expandedTextAd.setFinalUrlSuffix(Final_URL_suffix);

            // Create ad group ad.
            AdGroupAd expandedTextAdGroupAd = new AdGroupAd();
            expandedTextAdGroupAd.setAdGroupId(adGroupId);
            expandedTextAdGroupAd.setAd(expandedTextAd);

            expandedTextAdGroupAd.setStatus(AdGroupAdStatus.ENABLED);


            // Create the operation.
            AdGroupAdOperation adGroupAdOperation = new AdGroupAdOperation();
            adGroupAdOperation.setOperand(expandedTextAdGroupAd);
            adGroupAdOperation.setOperator(Operator.ADD);

            operations.add(adGroupAdOperation);

        // Add ads.
        AdGroupAdReturnValue result =
                adGroupAdService.mutate(operations.toArray(new AdGroupAdOperation[operations.size()]));

        // Display ads.
        for (AdGroupAd adGroupAdResult : result.getValue()) {
            ExpandedTextAd newAd = (ExpandedTextAd) adGroupAdResult.getAd();
            System.out.printf("Expanded text ad with ID %d and headline '%s - %s' was added.%n",
                    newAd.getId(), newAd.getHeadlinePart1(), newAd.getHeadlinePart2());
        }
        Main_class.in_or_out = true;
    }
}
