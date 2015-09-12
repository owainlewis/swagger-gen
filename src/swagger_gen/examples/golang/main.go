package main

// This code was autogenerated by swagger gen do not edit

import (
       "fmt"
       "log"
       "net/http"
       "github.com/gorilla/mux"
)

const PORT = ":8080"

/**
 * Get all pets
 */
func findPetsHandler(w http.ResponseWriter, r *http.Request) {
     // vars := mux.Vars(r)
     w.WriteHeader(http.StatusOK)
     fmt.Fprintln(w, "Get all pets")
}

/**
 * Create a new pet
 */
func addPetHandler(w http.ResponseWriter, r *http.Request) {
     // vars := mux.Vars(r)
     w.WriteHeader(http.StatusOK)
     fmt.Fprintln(w, "Create a new pet")
}

/**
 * Get a pet by ID
 */
func findPetByIdHandler(w http.ResponseWriter, r *http.Request) {
     // vars := mux.Vars(r)
     // id := vars["id"]
     w.WriteHeader(http.StatusOK)
     fmt.Fprintln(w, "Get a pet by ID")
}

/**
 * Delete a single pet by ID
 */
func deletePetHandler(w http.ResponseWriter, r *http.Request) {
     // vars := mux.Vars(r)
     // id := vars["id"]
     w.WriteHeader(http.StatusOK)
     fmt.Fprintln(w, "Delete a single pet by ID")
}

func main() {
     router := mux.NewRouter().StrictSlash(true)
     router.HandleFunc("/pets", findPetsHandler).Methods("GET")
     router.HandleFunc("/pets", addPetHandler).Methods("POST")
     router.HandleFunc("/pets/{id}", findPetByIdHandler).Methods("GET")
     router.HandleFunc("/pets/{id}", deletePetHandler).Methods("DELETE")
     log.Println("Server started. Listening on port " + PORT)
     log.Fatal(http.ListenAndServe(PORT, router))
}
