package admanager.axis.auth;

import static admanager.axis.auth.Get_campaigns.adWordsServices;
import static admanager.axis.auth.Get_campaigns.session;

import com.beust.jcommander.Parameter;
import com.google.api.ads.adwords.axis.v201806.cm.AdGroup;
import com.google.api.ads.adwords.axis.v201806.cm.AdGroupOperation;
import com.google.api.ads.adwords.axis.v201806.cm.AdGroupReturnValue;
import com.google.api.ads.adwords.axis.v201806.cm.AdGroupServiceInterface;
import com.google.api.ads.adwords.axis.v201806.cm.AdGroupStatus;
import com.google.api.ads.adwords.axis.v201806.cm.ApiError;
import com.google.api.ads.adwords.axis.v201806.cm.ApiException;
import com.google.api.ads.adwords.axis.v201806.cm.BiddingStrategyConfiguration;
import com.google.api.ads.adwords.axis.v201806.cm.Bids;
import com.google.api.ads.adwords.axis.v201806.cm.CpcBid;
import com.google.api.ads.adwords.axis.v201806.cm.Money;
import com.google.api.ads.adwords.axis.v201806.cm.Operator;
import com.google.api.ads.adwords.lib.client.AdWordsSession;
import com.google.api.ads.adwords.lib.factory.AdWordsServicesInterface;
import com.google.api.ads.adwords.lib.utils.examples.ArgumentNames;
import com.google.api.ads.common.lib.utils.examples.CodeSampleParams;

import java.rmi.RemoteException;

public class create_addgroups {

    public static long AddgroupID;

    private static class AddAdGroupsParams extends CodeSampleParams {
        @Parameter(names = ArgumentNames.CAMPAIGN_ID, required = true)
        private Long campaignId;
        private String adgroup_name;
        private Float bill;
    }

    public static void create_addgroups_main(String adgroup_name, Long campaignID, Float bill) {

        AddAdGroupsParams params = new AddAdGroupsParams();
        params.campaignId = campaignID;
        params.adgroup_name = adgroup_name;
        params.bill = bill;

        try {
            runExample(adWordsServices, session, params.campaignId, params.adgroup_name, params.bill);
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
                    Main_class.setApi_error(apiError);
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
     * @param session         the session.
     * @param campaignId      the ID of the campaign where the ad groups will be created.
     * @throws ApiException    if the API request failed with one or more service errors.
     * @throws RemoteException if the API request failed due to other errors.
     */
    private static void runExample(
            AdWordsServicesInterface adWordsServices, AdWordsSession session, long campaignId, String adgroup_name, float bill)
            throws RemoteException {
        // Get the AdGroupService.
        AdGroupServiceInterface adGroupService =
                adWordsServices.get(session, AdGroupServiceInterface.class);

        // Create ad group.
        AdGroup adGroup = new AdGroup();
        adGroup.setName(adgroup_name);
        adGroup.setStatus(AdGroupStatus.ENABLED);
        adGroup.setCampaignId(campaignId);

        // Create ad group bid.
        BiddingStrategyConfiguration biddingStrategyConfiguration = new BiddingStrategyConfiguration();
        Money cpcBidMoney = new Money();
        cpcBidMoney.setMicroAmount(cross_field(bill));
        CpcBid bid = new CpcBid();
        bid.setBid(cpcBidMoney);
        biddingStrategyConfiguration.setBids(new Bids[]{bid});
        adGroup.setBiddingStrategyConfiguration(biddingStrategyConfiguration);

        // Create operations.
        AdGroupOperation operation = new AdGroupOperation();
        operation.setOperand(adGroup);
        operation.setOperator(Operator.ADD);

        AdGroupOperation[] operations = new AdGroupOperation[]{operation};

        // Add ad groups.
        AdGroupReturnValue result = adGroupService.mutate(operations);

        // Display new ad groups.
        for (AdGroup adGroupResult : result.getValue()) {
            System.out.printf("Ad group with name '%s' and ID %d was added.%n",
                    adGroupResult.getName(), adGroupResult.getId());
            AddgroupID = adGroupResult.getId();
        }
    }

    public static Long cross_field(Float bill) {
        bill = 1000000 * bill;
        int a = Math.round(bill);
//        String temp = Integer.toString(a);
//        int already = 0;
//        for (int i = temp.length(); i > 3+already; ) {
//            temp = new StringBuilder(temp).insert(i-3-already, "_").toString();
//            already++;
//            i-=2;
//        }

        return Long.parseLong(String.valueOf(a));
    }
}
