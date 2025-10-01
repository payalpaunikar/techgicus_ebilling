package com.example.techgicus_ebilling.techgicus_ebilling.config;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.*;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.LimitType;
import com.example.techgicus_ebilling.techgicus_ebilling.exception.ResourceNotFoundException;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


@Component
public class DataSeeder implements ApplicationRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private FeatureRepository featureRepository;

    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;

    @Autowired
    private PlanFeatureLimitRepository planFeatureLimitRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${superadmin.name}")
    private String superAdminName;

    @Value("${superadmin.email}")
    private String superAdminEmailId;

    @Value("${superadmin.password}")
    private String superAdminPassword;


    @Transactional
    @Override
    public void run(ApplicationArguments args) throws Exception {
       seedRoles();
        seedFeatures();
       seedSubscriptionPlans();
      // assigneFeatureToPlan();
       seedPlanFeatureLimits();

       seedSuperAdmin();
    }

     private void seedRoles(){
        saveOrUpdateRole("ADMIN","Administration",true);
        saveOrUpdateRole("SUPERADMIN","Application owner",true);
        saveOrUpdateRole("SUBADMIN","Sub Administration",true);
     }

    private void saveOrUpdateRole(String name,String description,boolean isSystem){

        Role role = roleRepository.findByRoleName(name)
                .orElseGet(()-> new Role());
        role.setRoleName(name);
        role.setDescription(description);
        role.setSystemRole(isSystem);
        roleRepository.save(role);
    }


//    private void seedFeatures() {
//        List<SubscriptionFeature> features = List.of(
//                new SubscriptionFeature(null, "SYNC_DATA", "Sync data across devices", "Keep your data updated across all logged-in devices", true, new HashSet<>()),
//                new SubscriptionFeature(null, "MULTI_COMPANY", "Create multiple companies", "Manage multiple company profiles in one account", true, new HashSet<>()),
//                new SubscriptionFeature(null, "EWAY_BILLS", "Generate E-way Bills", "Generate GST-compliant e-way bills directly", true, new HashSet<>()),
//                new SubscriptionFeature(null, "GODOWN_MANAGEMENT", "Manage godowns & Transfer stock", "Track and transfer stock between godowns", true, new HashSet<>()),
//                new SubscriptionFeature(null, "RESTORE_TRANSACTIONS", "Restore deleted transactions", "Recover accidentally deleted transactions", true, new HashSet<>()),
//                new SubscriptionFeature(null, "REMOVE_ADS", "Remove advertisement on invoices", "Remove branding or ads from invoices", true, new HashSet<>()),
//                new SubscriptionFeature(null, "MULTI_PRICING", "Set multiple pricing for items", "Define different price lists for different customers or scenarios", true, new HashSet<>()),
//                new SubscriptionFeature(null, "WHATSAPP_TEMPLATES", "WhatsApp Marketing Templates", "Download and share unlimited marketing templates", true, new HashSet<>()),
//                new SubscriptionFeature(null, "BULK_UPDATE_ITEMS", "Update Items in bulk", "Edit item details in bulk instead of one-by-one", true, new HashSet<>()),
//                new SubscriptionFeature(null, "EXPORT_TALLY", "Export data to Tally", "Export financial data to Tally accounting software", true, new HashSet<>()),
//                new SubscriptionFeature(null, "LABEL_PRINTING", "Custom Label Printing", "Print custom item labels and barcodes", true, new HashSet<>()),
//                new SubscriptionFeature(null, "CREDIT_LIMIT", "Set credit limit for parties", "Limit the amount of credit extended to customers", true, new HashSet<>()),
//                new SubscriptionFeature(null, "FIXED_ASSETS", "Add Fixed Assets", "Track and manage company’s fixed assets", true, new HashSet<>()),
//                new SubscriptionFeature(null, "PAYMENT_REMINDERS", "Automate Payment Reminders", "Automatically remind customers of upcoming payments", true, new HashSet<>()),
//                new SubscriptionFeature(null, "COMBINE_ORDERS", "Combine multiple orders", "Merge multiple orders or challans into one invoice", true, new HashSet<>()),
//                new SubscriptionFeature(null, "POS_PAYMENTS", "Accept POS payments", "Enable Point-of-Sale payment methods", true, new HashSet<>()),
//                new SubscriptionFeature(null, "ACCOUNTING", "Accounting Module", "Enable full accounting features", true, new HashSet<>()),
//                new SubscriptionFeature(null, "SMART_CONNECT", "Unlock Ebilling Smart Connect", "Smart integration with other services", true, new HashSet<>()),
//
//                // REPORTS
//                new SubscriptionFeature(null, "BALANCE_SHEET", "Balance Sheet", "Generate standard balance sheet reports", true, new HashSet<>()),
//                new SubscriptionFeature(null, "BILLWISE_PNL", "Billwise P&L Reports", "Profit & Loss reports per bill", true, new HashSet<>()),
//                new SubscriptionFeature(null, "PARTYWISE_PNL", "Partywise P&L Report", "Profit & Loss reports per customer", true, new HashSet<>()),
//                new SubscriptionFeature(null, "BATCH_REPORT", "Item Batch Report", "Report of items by batch number", true, new HashSet<>()),
//                new SubscriptionFeature(null, "SERIAL_REPORT", "Item Serial Report", "Track items by serial numbers", true, new HashSet<>()),
//                new SubscriptionFeature(null, "STOCK_TRANSFER_REPORT", "Stock Transfer Report", "Stock transfers between godowns", true, new HashSet<>()),
//
//                // SETTINGS
//                new SubscriptionFeature(null, "TCS_ON_INVOICE", "Add TCS on invoices", "Apply Tax Collected at Source on invoices", true, new HashSet<>()),
//                new SubscriptionFeature(null, "PARTYWISE_RATE", "Different rates for each party", "Assign special pricing for different customers", true, new HashSet<>()),
//                new SubscriptionFeature(null, "MULTI_FIRM", "Create Multiple Firms", "Manage multiple firms under one account", true, new HashSet<>()),
//                new SubscriptionFeature(null, "INVOICE_PROFIT", "Check Profit on Invoices", "See margin/profit on each invoice", true, new HashSet<>()),
//                new SubscriptionFeature(null, "EXPENSE_ITC", "Input tax credit on expenses", "Claim ITC on business expenses", true, new HashSet<>()),
//                new SubscriptionFeature(null, "ITEM_EXTRA_FIELDS", "Extra Fields on Items", "Add additional item-level custom fields", true, new HashSet<>()),
//                new SubscriptionFeature(null, "TXN_MESSAGE_SELF", "Transaction message to self", "Receive a copy of transaction notification", true, new HashSet<>()),
//                new SubscriptionFeature(null, "TXN_UPDATE_ALERT", "Message on updated transactions", "Get alerts when a transaction is modified", true, new HashSet<>()),
//                new SubscriptionFeature(null, "TDS_ON_INVOICE", "Add TDS on invoices", "Apply Tax Deducted at Source", true, new HashSet<>()),
//                new SubscriptionFeature(null, "SERVICE_REMINDER", "Service Reminder", "Set reminders for periodic services", true, new HashSet<>()),
//                new SubscriptionFeature(null, "ITEM_CUSTOM_FIELDS", "Custom Fields for Items", "Define and use custom item fields", true, new HashSet<>())
//        );
//
//        for (SubscriptionFeature f : features) {
//            if (!featureRepo.existsByCode(f.getCode())) {
//                featureRepo.save(f);
//            }
//        }
//    }



    private void seedFeatures(){
        saveOrUpdateFeature("CREATE_MULTI_COMPANY","Create multiple companies","Manasge multiple company profiles in one account",true);
        saveOrUpdateFeature("EWAY_BILLS", "Generate E-way Bills", "Generate GST-compliant e-way bills directly", true);
        saveOrUpdateFeature("GENERATE_BALANCE_SHEET","Balance Sheet", "Generate standard balance sheet reports",true);
        saveOrUpdateFeature("BILLWISE_PNL","Billwise P&L Reports", "Profit & Loss reports per bill", true);
    }

    private void saveOrUpdateFeature(String code,String name,String description,boolean isPremium){
        Feature feature = featureRepository.findByCode(code)
                .orElseGet(()-> new Feature());

        feature.setCode(code);
        feature.setName(name);
        feature.setDescription(description);
        feature.setPremium(isPremium);

        featureRepository.save(feature);
    }


    private void seedSubscriptionPlans(){
       saveOrUpdateSubscriptionPlan("Silver",new BigDecimal(3999.0),365,"This is basic plan with minimal features");
       saveOrUpdateSubscriptionPlan("Gold",new BigDecimal(4299.0),365,"This is premium plan with all features");
       saveOrUpdateSubscriptionPlan("Lifetime",new BigDecimal(4299.00),null,"This is premium plan with all features as well as lifetime accessbility.");
    }

    private void saveOrUpdateSubscriptionPlan(String name, BigDecimal price,Integer durationDays,String description){
        SubscriptionPlan subscriptionPlan = subscriptionPlanRepository.findByName(name)
                .orElseGet(()-> new SubscriptionPlan());

        subscriptionPlan.setName(name);
        subscriptionPlan.setPrice(price);
        subscriptionPlan.setDescription(description);
        subscriptionPlan.setDurationDays(durationDays);

        subscriptionPlanRepository.save(subscriptionPlan);
    }


//    private void assigneFeatureToPlan(){
//
//        // Fetch features
//        Feature multiCompany = featureRepository.findByCode("CREATE_MULTI_COMPANY").orElseThrow();
//        Feature ewayBills = featureRepository.findByCode("EWAY_BILLS").orElseThrow();
//        Feature balanceSheet = featureRepository.findByCode("GENERATE_BALANCE_SHEET").orElseThrow();
//        Feature billwisePnl = featureRepository.findByCode("BILLWISE_PNL").orElseThrow();
//
//        // Fetch plans
//        SubscriptionPlan silver = subscriptionPlanRepository.findByName("Silver").orElseThrow();
//        SubscriptionPlan gold = subscriptionPlanRepository.findByName("Gold").orElseThrow();
//        SubscriptionPlan lifetime = subscriptionPlanRepository.findByName("Lifetime").orElseThrow();
//
//        // assign feature
//        silver.addFeatures(multiCompany);
//        silver.addFeatures(ewayBills);
//        silver.addFeatures(balanceSheet);
//        silver.addFeatures(billwisePnl);
//
//        gold.addFeatures(multiCompany);
//        gold.addFeatures(ewayBills);
//        gold.addFeatures(balanceSheet);
//        gold.addFeatures(billwisePnl);
//
//        lifetime.addFeatures(multiCompany);
//        lifetime.addFeatures(ewayBills);
//        lifetime.addFeatures(balanceSheet);
//        lifetime.addFeatures(billwisePnl);
//
//        subscriptionPlanRepository.saveAll(List.of(silver,gold,lifetime));
//
//    }


    private PlanFeatureLimit createIfNotExists(Feature feature,LimitType type,
                                            Integer usageLimit,Boolean booleanLimit,
                                              SubscriptionPlan plan){

        return planFeatureLimitRepository.findBySubscriptionPlanAndFeature(plan,feature)
                .orElseGet(()->{
                    PlanFeatureLimit planFeatureLimit = new PlanFeatureLimit();
                    planFeatureLimit.setSubscriptionPlan(plan);
                    planFeatureLimit.setFeature(feature);
                    planFeatureLimit.setLimitType(type);
                    planFeatureLimit.setUsageLimit(usageLimit);
                    planFeatureLimit.setBooleanLimit(booleanLimit);
                    return planFeatureLimitRepository.save(planFeatureLimit);
                });

    }


    private void seedPlanFeatureLimits(){

        // Fetch features
        Feature multiCompany = featureRepository.findByCode("CREATE_MULTI_COMPANY").orElseThrow();
        Feature ewayBills = featureRepository.findByCode("EWAY_BILLS").orElseThrow();
        Feature balanceSheet = featureRepository.findByCode("GENERATE_BALANCE_SHEET").orElseThrow();
        Feature billwisePnl = featureRepository.findByCode("BILLWISE_PNL").orElseThrow();

        // Fetch plans
        SubscriptionPlan silver = subscriptionPlanRepository.findByName("Silver").orElseThrow();
        SubscriptionPlan gold = subscriptionPlanRepository.findByName("Gold").orElseThrow();
        SubscriptionPlan lifetime = subscriptionPlanRepository.findByName("Lifetime").orElseThrow();

        // insert only if not exits

        //for siliver
        createIfNotExists(multiCompany,LimitType.INTEGER,3,null,silver);
        createIfNotExists(ewayBills,LimitType.BOOLEAN,null,true,silver);
        createIfNotExists(balanceSheet,LimitType.BOOLEAN,null,true,silver);
        createIfNotExists(billwisePnl,LimitType.BOOLEAN,null,true,silver);

       //for gold
        createIfNotExists(multiCompany, LimitType.INTEGER, 5, null, gold);
        createIfNotExists(ewayBills, LimitType.BOOLEAN, null, true, gold);
        createIfNotExists(balanceSheet, LimitType.BOOLEAN, null, true, gold);
        createIfNotExists(billwisePnl, LimitType.BOOLEAN, null, true, gold);

        //for lifetime
        createIfNotExists(multiCompany, LimitType.INTEGER, 5, null, lifetime);
        createIfNotExists(ewayBills, LimitType.BOOLEAN, null, true, lifetime);
        createIfNotExists(balanceSheet, LimitType.BOOLEAN, null, true, lifetime);
        createIfNotExists(billwisePnl, LimitType.BOOLEAN, null, true, lifetime);
    }


//    // if the limit type is integer
//    private PlanFeatureLimit setPlanFeatureLimit(Feature feature,LimitType limitType,
//                                     Integer usageLimit,SubscriptionPlan subscriptionPlan ){
//        PlanFeatureLimit silverPlanLimit = new PlanFeatureLimit();
//        silverPlanLimit.setFeature(feature);
//        silverPlanLimit.setLimitType(limitType);
//        silverPlanLimit.setUsageLimit(usageLimit);
//        silverPlanLimit.setSubscriptionPlan(subscriptionPlan);
//
//        return silverPlanLimit;
//    }
//
//
//    // if the limit type is boolean
//    private PlanFeatureLimit setPlanFeatureLimit(Feature feature,LimitType limitType,
//                                                 boolean booleanLimit,SubscriptionPlan subscriptionPlan ){
//        PlanFeatureLimit silverPlanLimit = new PlanFeatureLimit();
//        silverPlanLimit.setFeature(feature);
//        silverPlanLimit.setLimitType(limitType);
//        silverPlanLimit.setBooleanLimit(booleanLimit);
//        silverPlanLimit.setSubscriptionPlan(subscriptionPlan);
//
//        return silverPlanLimit;
//    }

    private void seedSuperAdmin(){

        Role superAdminRole = roleRepository.findByRoleName("SUPERADMIN")
                .orElseThrow(()-> new ResourceNotFoundException("Super Admin role is not found"));


        if (userRepository.findByEmail(superAdminEmailId).isEmpty()) {

            User superAdmin = new User();
            superAdmin.setUserName(superAdminName);
            superAdmin.setEmail(superAdminEmailId);
            superAdmin.setPassword(passwordEncoder.encode(superAdminPassword));
            superAdmin.setRoles(Set.of(superAdminRole));
            superAdmin.setCreatedAt(LocalDateTime.now());
            superAdmin.setUpdateAt(LocalDateTime.now());

            userRepository.save(superAdmin);
        }
    }


}
