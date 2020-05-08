# Syntax -

## Function Representation

Used to represent a function in the definition file. The design is similar to an object in javascript.
```javascript
function f1 = {
    name:"comp1",
    handler:"comp1/app.js",
    language:”nodejs”,
};
```

  

## Function orchestration definition

Orchestrator is the code block that defines the execution of the function. The scope defines the runtime code block if the application. The orchestrator has a parameter block and a code block. Parameter block could contain zero or multiple parameter variables. Parameter variable is a special variable that is used to define an incoming request data field in the code in a cleaner way. These variables can be used in the scope to change the execution of the functions. Orchestrator code block could contain one or more imported orchestration construct, Sequence construct, Parallel construct or If construct.

    orchestrate ($type) {
	    //orchestration flow goes here
    }

  

## Sequence Representation
Sequence construct defines sequence execution of multiple functions. The design is similar to an array in c like programming languages.


    orchestrate ($type) {
	    sequence s1 = [f1 f2];
    }

  

## Parallel Representation

Parallel construct defines parallel execution of multiple functions. The design is similar to an array in c like programming languages.

Code -

    orchestrate ($type) {
	    parallel s1 = [f1 f2];
    }

## Choice Representation

Represented by If keyword. If construct defines a choice of execution depending on a certain given condition. The design is identical to If statements in c based languages.However the condition evaluation logic is different and done according to the cloud provider.

    orchestrate ($type) {
	    if (!($type=="push")) {
		    sequence s1 = [f1 f2];
	    } else if ($type==”pull”) {
		    sequence s2 = [f1 f3];
	    } else {
		    sequence s3 = [f2 f3];
	    }
    }

  

## Import Representation

Import construct allows developers to make use of another FaaS module’s resources and orchestrators.


    import comp1;
    
    orchestrate () {
	    start comp1;
    }

# Connectors

Connectors are extended modules of the language core that transforms the vendor neutral function orchestrations to vendor specific code. The language core is completely decoupled from the connectors. Each cloud has prefered way of function coordination and each connector is designed to transform the vendor neutral code into vendor specific native code with the best practices.

#### AWS

AWS connector is used to generate AWS specific serverless deployment artifacts from defined language sources. The connector primarily follows AWS Cloudformation and AWS Step functions language to create the deployment artifacts. AWS step functions will be used to define the function orchestration. AWS Cloudformation will be used to create the necessary functions, state machine and IAM rules required for the orchestration. Finally, it creates API Gateway as the entry point for the orchestration.

  

This approach follows the deployment best practices of AWS. Using cloud formations and Step functions does not add any extra cost for the developer. Instead it adds features such as one click deployment rollback and logs. Step Functions optimizes performance of the functions on the orchestrations and provides orchestration ability with no extra cost for the state machine behavior.

  

#### Knative

Knative connector is used to generate the Knative deployment artifacts for kubernetes serverless workloads from the defined language sources. Unlike AWS’s statemachine approach, Knative uses event driven approach for function orchestration. Knative requires to register function execution in the control plane as separate events.

  

The knative connector implementation involves defining knative services for each function, Implementing eventing sequences and parallel operations and Choice condition behaviour from knative function custom functions. Finally an Ingress should be created as a gateway to expose the created orchestrator to outside the cluster.

  

# Compiling FaaS File

Build Command

    fass build aws

# Project Structure-
    .
    
    ├── comp1
    │ ├── app.js
    │ └── comp1.faas
    ├── comp2
    │ ├── app.js
    │ └── comp2.faas
    ├── demo.faas
    └── target
    ├── aws
    │   └── demo-cloudformation.json
    └── knative
        └── demo-knative-deployment.json

