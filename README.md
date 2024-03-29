##  The Metal App

This app will mint and transfer metal tokens.

You can:
1. Write a flow to Create a Metal Asset and its State on the Ledger. `IssueMetalTokensFlow`
2. List metal entries. `ListMetalTokens`
3. Claim and transfer the tokens to another member. `TransferMetalTokenFlow`
4. Burn tokens available to a member. `BurnMetalTokenFlow`


## Setup

1. We will begin our test deployment with clicking the `startCorda`. This task will load up the combined Corda workers in docker.
   A successful deployment will allow you to open the REST APIs at: https://localhost:8888/api/v1/swagger#. You can test out some of the
   functions to check connectivity. (GET /cpi function call should return an empty list as for now.)
2. We will now deploy the cordapp with a click of `5-vNodeSetup` task. Upon successful deployment of the CPI, the GET /cpi function call should now return the meta data of the cpi you just upload


## Running the app

Trigger flows using `POST /flow/{holdingidentityshorthash}` and view results at `GET /flow/{holdingidentityshorthash}/{clientrequestid}`
* holdingidentityshorthash: the id of the network participants, ie Bob, Alice, Charlie. You can view all the short hashes of the network member with another gradle task called `ListVNodes`
* clientrequestid: the id you specify in the flow requestBody when the flow was triggered.


## Step 1: Create Metal State
Choose a VNode identity, and get its short hash. (Let's pick Alice.).

Go to `POST /flow/{holdingidentityshorthash}`, enter the identity short hash(Alice's hash) and request body:
```
{
    "clientRequestId": "issue-1",
    "flowClassName": "com.r3.developers.samples.tokens.workflows.IssueMetalTokenFlow",
    "requestBody": {
        "symbol": "METAL",
        "owner": "CN=Bob, OU=Test Dept, O=R3, L=London, C=GB",
        "amount": "20"
    }
}
```

Upon triggering the IssueMetalTokensFlow flow, go to `GET /flow/{holdingidentityshorthash}/{clientrequestid}` and enter the short hash(Alice's hash) and clientrequestid to view the flow result


## Step 2: List the metal state
Go to `POST /flow/{holdingidentityshorthash}`, enter the identity short hash(Bob's hash) and request body:
```
{
    "clientRequestId": "list-1",
    "flowClassName": "com.r3.developers.samples.tokens.workflows.ListMetalTokens",
    "requestBody": {}
}
```
Upon triggering the ListMetalTokens flow, again, we need to hop to `GET /flow/{holdingidentityshorthash}/{clientrequestid}`
and check the result.


## Step 3: Transfer using `TransferMetalTokenFlow`
In this step, Bob will transfer some tokens from his vault to Charlie.
Goto `POST /flow/{holdingidentityshorthash}`, enter the identity short hash and request body.
Use Bob's holdingidentityshorthash to fire this post API.
```
{
    "clientRequestId": "transfer-1",
    "flowClassName": "com.r3.developers.samples.tokens.workflows.TransferMetalTokenFlow",
    "requestBody": {
        "symbol": "METAL",
        "issuer": "CN=Alice, OU=Test Dept, O=R3, L=London, C=GB",
        "receiver": "CN=Charlie, OU=Test Dept, O=R3, L=London, C=GB",
        "amount": "5"
        }
}
```
For the result of this flow, go to `GET /flow/{holdingidentityshorthash}/{clientrequestid}`  entering the required fields.


## Step 4: Confirm token balances of Bob and Charlie
Go to `POST /flow/{holdingidentityshorthash}`, enter the identity short hash(Bob's hash) and request body:
```
{
    "clientRequestId": "list-2",
    "flowClassName": "com.r3.developers.samples.tokens.workflows.ListMetalTokens",
    "requestBody": {}
}
```
Go to `POST /flow/{holdingidentityshorthash}`, enter the identity short hash(Charlie's hash) and request body:
```
{
    "clientRequestId": "list-3",
    "flowClassName": "com.r3.developers.samples.tokens.workflows.ListMetalTokens",
    "requestBody": {}
}
```

And as for the result, you need to go to the Get API again and enter the short hash and client request ID.


## Step 5: Burn metal token using BurnMetalTokenFlow
Go to `POST /flow/{holdingidentityshorthash}`, enter the identity short hash(Bob's hash) and request body:
```
{
    "clientRequestId": "burn-1",
    "flowClassName": "com.r3.developers.samples.tokens.workflows.BurnMetalTokenFlow",
    "requestBody": {
        "symbol": "METAL",
        "issuer": "CN=Alice, OU=Test Dept, O=R3, L=London, C=GB",
        "amount": "5"
        }
}
```
Go to `GET /flow/{holdingidentityshorthash}/{clientrequestid}` and enter the required fields to check the result of
the flow.


## Step 4: Confirm balance of Bob

Go to `POST /flow/{holdingidentityshorthash}`, enter the identity short hash(Bob's hash) and request body:
```
{
    "clientRequestId": "list-4",
    "flowClassName": "com.r3.developers.samples.tokens.workflows.ListMetalTokens",
    "requestBody": {}
}
```

To get the result use the Get API entering the short hash and client request ID.

