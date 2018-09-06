package admanager.axis.auth;

import com.beust.jcommander.Parameter;
import com.google.api.ads.adwords.axis.v201806.cm.AdGroup;
import com.google.api.ads.adwords.axis.v201806.cm.AdGroupOperation;
import com.google.api.ads.adwords.axis.v201806.cm.AdGroupReturnValue;
import com.google.api.ads.adwords.axis.v201806.cm.AdGroupServiceInterface;
import com.google.api.ads.adwords.axis.v201806.cm.AdGroupStatus;
import com.google.api.ads.adwords.axis.v201806.cm.ApiError;
import com.google.api.ads.adwords.axis.v201806.cm.ApiException;
import com.google.api.ads.adwords.axis.v201806.cm.Operator;
import com.google.api.ads.adwords.lib.client.AdWordsSession;
import com.google.api.ads.adwords.lib.factory.AdWordsServicesInterface;
import com.google.api.ads.adwords.lib.utils.examples.ArgumentNames;
import com.google.api.ads.common.lib.utils.examples.CodeSampleParams;
import java.rmi.RemoteException;
import static admanager.axis.auth.Get_campaigns.adWordsServices;
import static admanager.axis.auth.Get_campaigns.session;

public class remove_add_group {

    private static class RemoveAdGroupParams extends CodeSampleParams {
        @Parameter(names = ArgumentNames.AD_GROUP_ID, required = true)
        private Long adGroupId;
    }

    public static void remove_add_group_main(long id){


        RemoveAdGroupParams params = new RemoveAdGroupParams();
            // Either pass the required parameters for this example on the command line, or insert them
            // into the code here. See the parameter class definition above for descriptions.
            params.adGroupId = id;

        try {
            runExample(adWordsServices, session, params.adGroupId);
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
     * @param adGroupId the ID of the ad group to remove.
     * @throws ApiException if the API request failed with one or more service errors.
     * @throws RemoteException if the API request failed due to other errors.
     */
    public static void runExample(
            AdWordsServicesInterface adWordsServices, AdWordsSession session, long adGroupId)
            throws RemoteException {
        // Get the AdGroupService.
        AdGroupServiceInterface adGroupService =
                adWordsServices.get(session, AdGroupServiceInterface.class);

        // Create ad group with REMOVED status.
        AdGroup adGroup = new AdGroup();
        adGroup.setId(adGroupId);
        adGroup.setStatus(AdGroupStatus.REMOVED);

        // Create operations.
        AdGroupOperation operation = new AdGroupOperation();
        operation.setOperand(adGroup);
        operation.setOperator(Operator.SET);

        AdGroupOperation[] operations = new AdGroupOperation[] {operation};

        // Remove ad group.
        AdGroupReturnValue result = adGroupService.mutate(operations);

        // Display ad groups.
        for (AdGroup adGroupResult : result.getValue()) {
            System.out.printf("Ad group with name '%s' and ID %d was removed.%n",
                    adGroupResult.getName(), adGroupResult.getId());
        }
        Main_class.in_or_out = false;
    }


}
