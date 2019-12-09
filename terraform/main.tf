# Specify the provider and access details
provider "aws" {
  region     = "us-east-1"
  access_key = "${var.aws-access-key}"
  secret_key = "${var.aws-secret-key}"
  version = "~> 2.0"
}

locals {
  public_key_filename  = "${path.root}/keys/id_rsa.pub"
  private_key_filename = "${path.root}/keys/id_rsa"
}

# Generate an RSA key to be used
resource "tls_private_key" "generated" {
  algorithm = "RSA"
}

# Generate the local SSH Key pair in the directory specified
resource "local_file" "public_key_openssh" {
  content  = "${tls_private_key.generated.public_key_openssh}"
  filename = "${local.public_key_filename}"
}
resource "local_file" "private_key_pem" {
  content  = "${tls_private_key.generated.private_key_pem}"
  filename = "${local.private_key_filename}"
}

resource "aws_key_pair" "generated" {
  key_name   = "pjsk-sshtest-${uuid()}"
  public_key = "${tls_private_key.generated.public_key_openssh}"

  lifecycle {
    ignore_changes = ["key_name"]
  }
}

 
# Default vpc
resource "aws_vpc" "default" {
  cidr_block = "10.0.0.0/16"
}

# Our default security group to for the database
resource "aws_security_group" "mongo" {
  description = "security group created from terraform"
  vpc_id      = "vpc-c422e2a0" # only valid for us-east-1

  # SSH access from anywhere
  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  
  # mongodb access from anywhere
  ingress {
    from_port   = 27017
    to_port     = 27017
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  # outbound internet access
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_instance" "mongodb" {
  # The connection block tells our provisioner how to
  # communicate with the resource (instance)
  connection {
    # The default username for our AMI
    user = "ubuntu"
    private_key = "${tls_private_key.generated.private_key_pem}"
  }

  instance_type = "t2.micro"
  
  tags = { Name = "mongodb instance" } 

  # standard realmethods community image with mongo started on the default port 
  ami = "ami-0e2a167cf2e0ce6c0"

  # The name of the SSH keypair you've created and downloaded
  # from the AWS console.
  #
  # https://console.aws.amazon.com/ec2/v2/home?region=us-west-2#KeyPairs:
  #
  # key_name = ""
  key_name = "${aws_key_pair.generated.key_name}"
  
  # Our Security group to allow HTTP and SSH access
  vpc_security_group_ids = ["${aws_security_group.mongo.id}"]
  
  # To ensure ssh access works
    provisioner "remote-exec" {
    inline = [
      "sudo ls",
    ]
  }
}

resource "aws_lambda_function" "createPlayer" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_createPlayer"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.PlayerAWSLambdaDelegate::createPlayer"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "getPlayer" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_getPlayer"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.PlayerAWSLambdaDelegate::getPlayer"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "getAllPlayer" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_getAllPlayer"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.PlayerAWSLambdaDelegate::getAllPlayer"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "savePlayer" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_savePlayer"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.PlayerAWSLambdaDelegate::savePlayer"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "deletePlayer" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_deletePlayer"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.PlayerAWSLambdaDelegate::deletePlayer"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "getLeaguePlayers" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_getLeaguePlayers"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.LeagueAWSLambdaDelegate::getPlayers"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "addLeaguePlayers" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_addLeaguePlayers"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.LeagueAWSLambdaDelegate::addPlayers"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "assignLeaguePlayers" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_assignLeaguePlayers"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.LeagueAWSLambdaDelegate::assignPlayers"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "deleteLeaguePlayers" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_deleteLeaguePlayers"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.LeagueAWSLambdaDelegate::deletePlayers"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "createLeague" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_createLeague"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.LeagueAWSLambdaDelegate::createLeague"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "getLeague" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_getLeague"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.LeagueAWSLambdaDelegate::getLeague"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "getAllLeague" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_getAllLeague"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.LeagueAWSLambdaDelegate::getAllLeague"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "saveLeague" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_saveLeague"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.LeagueAWSLambdaDelegate::saveLeague"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "deleteLeague" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_deleteLeague"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.LeagueAWSLambdaDelegate::deleteLeague"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "getTournamentMatchups" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_getTournamentMatchups"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.TournamentAWSLambdaDelegate::getMatchups"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "addTournamentMatchups" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_addTournamentMatchups"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.TournamentAWSLambdaDelegate::addMatchups"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "assignTournamentMatchups" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_assignTournamentMatchups"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.TournamentAWSLambdaDelegate::assignMatchups"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "deleteTournamentMatchups" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_deleteTournamentMatchups"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.TournamentAWSLambdaDelegate::deleteMatchups"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "createTournament" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_createTournament"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.TournamentAWSLambdaDelegate::createTournament"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "getTournament" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_getTournament"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.TournamentAWSLambdaDelegate::getTournament"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "getAllTournament" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_getAllTournament"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.TournamentAWSLambdaDelegate::getAllTournament"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "saveTournament" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_saveTournament"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.TournamentAWSLambdaDelegate::saveTournament"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "deleteTournament" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_deleteTournament"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.TournamentAWSLambdaDelegate::deleteTournament"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "getMatchupGames" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_getMatchupGames"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.MatchupAWSLambdaDelegate::getGames"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "addMatchupGames" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_addMatchupGames"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.MatchupAWSLambdaDelegate::addGames"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "assignMatchupGames" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_assignMatchupGames"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.MatchupAWSLambdaDelegate::assignGames"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "deleteMatchupGames" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_deleteMatchupGames"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.MatchupAWSLambdaDelegate::deleteGames"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "createMatchup" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_createMatchup"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.MatchupAWSLambdaDelegate::createMatchup"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "getMatchup" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_getMatchup"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.MatchupAWSLambdaDelegate::getMatchup"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "getAllMatchup" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_getAllMatchup"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.MatchupAWSLambdaDelegate::getAllMatchup"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "saveMatchup" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_saveMatchup"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.MatchupAWSLambdaDelegate::saveMatchup"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "deleteMatchup" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_deleteMatchup"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.MatchupAWSLambdaDelegate::deleteMatchup"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "getGamePlayer" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_getGamePlayer"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.GameAWSLambdaDelegate::getPlayer"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "saveGamePlayer" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_saveGamePlayer"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.GameAWSLambdaDelegate::savePlayer"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "deleteGamePlayer" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_deleteGamePlayer"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.GameAWSLambdaDelegate::deletePlayer"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "createGame" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_createGame"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.GameAWSLambdaDelegate::createGame"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "getGame" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_getGame"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.GameAWSLambdaDelegate::getGame"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "getAllGame" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_getAllGame"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.GameAWSLambdaDelegate::getAllGame"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "saveGame" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_saveGame"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.GameAWSLambdaDelegate::saveGame"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "deleteGame" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_deleteGame"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.GameAWSLambdaDelegate::deleteGame"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "getAlleyLeagues" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_getAlleyLeagues"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.AlleyAWSLambdaDelegate::getLeagues"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "addAlleyLeagues" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_addAlleyLeagues"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.AlleyAWSLambdaDelegate::addLeagues"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "assignAlleyLeagues" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_assignAlleyLeagues"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.AlleyAWSLambdaDelegate::assignLeagues"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "deleteAlleyLeagues" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_deleteAlleyLeagues"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.AlleyAWSLambdaDelegate::deleteLeagues"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "getAlleyTournaments" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_getAlleyTournaments"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.AlleyAWSLambdaDelegate::getTournaments"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "addAlleyTournaments" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_addAlleyTournaments"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.AlleyAWSLambdaDelegate::addTournaments"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "assignAlleyTournaments" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_assignAlleyTournaments"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.AlleyAWSLambdaDelegate::assignTournaments"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "deleteAlleyTournaments" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_deleteAlleyTournaments"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.AlleyAWSLambdaDelegate::deleteTournaments"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "getAlleyLanes" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_getAlleyLanes"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.AlleyAWSLambdaDelegate::getLanes"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "addAlleyLanes" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_addAlleyLanes"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.AlleyAWSLambdaDelegate::addLanes"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "assignAlleyLanes" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_assignAlleyLanes"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.AlleyAWSLambdaDelegate::assignLanes"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "deleteAlleyLanes" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_deleteAlleyLanes"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.AlleyAWSLambdaDelegate::deleteLanes"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "createAlley" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_createAlley"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.AlleyAWSLambdaDelegate::createAlley"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "getAlley" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_getAlley"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.AlleyAWSLambdaDelegate::getAlley"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "getAllAlley" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_getAllAlley"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.AlleyAWSLambdaDelegate::getAllAlley"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "saveAlley" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_saveAlley"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.AlleyAWSLambdaDelegate::saveAlley"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "deleteAlley" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_deleteAlley"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.AlleyAWSLambdaDelegate::deleteAlley"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "createLane" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_createLane"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.LaneAWSLambdaDelegate::createLane"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "getLane" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_getLane"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.LaneAWSLambdaDelegate::getLane"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "getAllLane" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_getAllLane"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.LaneAWSLambdaDelegate::getAllLane"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "saveLane" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_saveLane"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.LaneAWSLambdaDelegate::saveLane"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}

resource "aws_lambda_function" "deleteLane" {
  filename         = "/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar"
  function_name    = "lambdaFunc_deleteLane"
  role             = "arn:aws:iam::110777515443:role/service-role/myRoleName"
  handler          = "com.freeport.delegate.LaneAWSLambdaDelegate::deleteLane"
  source_code_hash = "${filebase64sha256("/home/circleci/gitRoot/target/lambdamongodemo-0.0.1.jar")}"
  runtime          = "java8"
  memory_size      = "512"
  timeout          = "30"
  publish          = "true"
  environment {
    variables = {
      mongoDbServerAddresses = "${aws_instance.mongodb.public_ip}:27017"
    }
  }
  vpc_config {
     subnet_ids = [""]
     security_group_ids = [""]
  }  
}




