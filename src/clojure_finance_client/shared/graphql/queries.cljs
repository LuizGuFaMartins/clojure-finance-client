(ns clojure-finance-client.shared.graphql.queries)

(def list-my-transactions
  "query GetMyTransactions {
    my_transactions {
     id
     amount
     status
     created_at
     from_user {
       id
       email
     }
     to_user {
       id
       email
     }
   }
  }")

(def create-transaction
  "mutation CreateTransaction($input: TransactionInput!) {
    create_transaction(input: $input) {
      id
      status
    }
  }")