# Duplicate Observed Data

**URL:** https://refactoring.guru/duplicate-observed-data

## Problem
Domain data is stored in classes responsible for the GUI, and domain methods need access to this data.

## Solution
Separate the data into a domain class and ensure synchronization between the domain class and the GUI using the Observer pattern.

## Why Refactor
- **Separation of Concerns:** Business logic should be independent of the user interface.
- **Testability:** Domain logic is much easier to test without the GUI.
- **Flexibility:** Allows for multiple views or interfaces for the same underlying data.
