(ns clojure-finance-client.shared.graphql.queries)

(def list-my-transactions
  "query GetMyTransactions($days: Int, $type: String) {
    my_transactions(days: $days, type: $type) {
     id
     amount
     status
     created_at
     from_user {
       id
       name
     }
     to_user {
       id
       name
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