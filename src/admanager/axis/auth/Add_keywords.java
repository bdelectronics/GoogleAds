package admanager.axis.auth;


import com.beust.jcommander.Parameter;
import com.google.api.ads.adwords.axis.v201806.cm.AdGroupCriterion;
import com.google.api.ads.adwords.axis.v201806.cm.AdGroupCriterionOperation;
import com.google.api.ads.adwords.axis.v201806.cm.AdGroupCriterionReturnValue;
import com.google.api.ads.adwords.axis.v201806.cm.AdGroupCriterionServiceInterface;
import com.google.api.ads.adwords.axis.v201806.cm.ApiError;
import com.google.api.ads.adwords.axis.v201806.cm.ApiException;
import com.google.api.ads.adwords.axis.v201806.cm.BiddableAdGroupCriterion;
import com.google.api.ads.adwords.axis.v201806.cm.Keyword;
import com.google.api.ads.adwords.axis.v201806.cm.KeywordMatchType;
import com.google.api.ads.adwords.axis.v201806.cm.Operator;
import com.google.api.ads.adwords.axis.v201806.cm.UserStatus;
import com.google.api.ads.adwords.lib.client.AdWordsSession;
import com.google.api.ads.adwords.lib.factory.AdWordsServicesInterface;
import com.google.api.ads.adwords.lib.utils.examples.ArgumentNames;
import com.google.api.ads.common.lib.utils.examples.CodeSampleParams;
import static admanager.axis.auth.Get_campaigns.adWordsServices;
import static admanager.axis.auth.Get_campaigns.session;
import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;

public class Add_keywords {

    private static class AddKeywordsParams extends CodeSampleParams {
        @Parameter(names = ArgumentNames.AD_GROUP_ID, required = true)
        private Long adGroupId;
        private String Text;
    }

    public static void keywords_main(Long AddgroupID, String Text){



        AddKeywordsParams params = new AddKeywordsParams();
            // Either pass the required parameters for this example on the command line, or insert them
            // into the code here. See the parameter class definition above for descriptions.
            params.adGroupId = AddgroupID;
            params.Text = Text;

        try {
            runExample(adWordsServices, session, params.adGroupId, params.Text);
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
            Main_class.error += 1;
        } catch (RemoteException re) {
            System.err.printf("Request failed unexpectedly due to RemoteException: %s%n", re);
            Main_class.error += 1;
        } catch (UnsupportedEncodingException ue) {
            System.err.printf("Example failed due to encoding exception: %s%n", ue);
            Main_class.error += 1;
        }

    }

    /**
     * Runs the example.
     *
     * @param adWordsServices the services factory.
     * @param session the session.
     * @param adGroupId the ID of the ad group where the keywords will be created.
     * @throws ApiException if the API request failed with one or more service errors.
     * @throws RemoteException if the API request failed due to other errors.
     * @throws UnsupportedEncodingException if encoding the final URL failed.
     */
    public static void runExample(
            AdWordsServicesInterface adWordsServices, AdWordsSession session, long adGroupId, String Text)
            throws RemoteException, UnsupportedEncodingException {
        // Get the AdGroupCriterionService.
        AdGroupCriterionServiceInterface adGroupCriterionService =
                adWordsServices.get(session, AdGroupCriterionServiceInterface.class);

        // Create keywords.
        Keyword keyword1 = new Keyword();
        keyword1.setText(Text);
        keyword1.setMatchType(KeywordMatchType.EXACT);

        // Create biddable ad group criterion.
        BiddableAdGroupCriterion keywordBiddableAdGroupCriterion1 = new BiddableAdGroupCriterion();
        keywordBiddableAdGroupCriterion1.setAdGroupId(adGroupId);
        keywordBiddableAdGroupCriterion1.setCriterion(keyword1);

        // You can optionally provide these field(s).
        keywordBiddableAdGroupCriterion1.setUserStatus(UserStatus.ENABLED);

        // Create operations.
        AdGroupCriterionOperation keywordAdGroupCriterionOperation1 = new AdGroupCriterionOperation();
        keywordAdGroupCriterionOperation1.setOperand(keywordBiddableAdGroupCriterion1);
        keywordAdGroupCriterionOperation1.setOperator(Operator.ADD);

        AdGroupCriterionOperation[] operations =
                new AdGroupCriterionOperation[] {keywordAdGroupCriterionOperation1};

        // Add keywords.
        AdGroupCriterionReturnValue result = adGroupCriterionService.mutate(operations);

        // Display results.
        for (AdGroupCriterion adGroupCriterionResult : result.getValue()) {
            System.out.printf("Keyword ad group criterion with ad group ID %d, criterion ID %d, "
                            + "text '%s', and match type '%s' was added.%n", adGroupCriterionResult.getAdGroupId(),
                    adGroupCriterionResult.getCriterion().getId(),
                    ((Keyword) adGroupCriterionResult.getCriterion()).getText(),
                    ((Keyword) adGroupCriterionResult.getCriterion()).getMatchType());
        }
    }

}
