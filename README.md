# LLM Process Automation Prototype

This repository contains a prototype developed as part of a Master's thesis at the University of Economics in Prague (VŠE), focused on the use of large language models (LLMs) in intelligent process automation (IPA).

The prototype evaluates how LLMs can be used as a **tool within deterministic workflows** for classification and structured data extraction in a telecom domain.

---

## 🎯 Goal

The goal of this project is to experimentally verify how LLMs can be integrated into process automation scenarios, specifically:

- classification of incoming requests
- structured data extraction

The prototype operates on synthetic telecom data and evaluates model behavior under varying conditions (noise, completeness, channel format).

---

## 🧠 Architecture Overview

The project is divided into two main logical parts:

### 1. Core Processing Services
Located in the main application:

- **Classification Service**
    - Determines request type from input text
- **Extraction Service**
    - Extracts structured DTOs based on request type

Both services:
- use prompt-based strategies
- operate as **LLM-as-a-tool components** within predefined workflows

Two prompt strategies are implemented:
- `direct` (baseline)
- `few-shot` (enhanced with examples)

---

### 2. Evaluation / Validation Layer

- **Validation Services (2x)**
    - Execute classification and extraction over benchmark datasets
    - Compare outputs with expected results
    - Store evaluation outputs

Outputs are written to: