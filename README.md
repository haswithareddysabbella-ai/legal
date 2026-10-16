# LegalSearch — Intelligent Legal Document Analytics Platform

This Eclipse-ready Java project demonstrates the six DSA modules from the LegalSearch project brief. It provides:
- a Java console menu;
- a local browser interface served by Java at `http://localhost:8080`;
- CSV files for cases, legal documents, lawyers, assignments, and search history;
- algorithm examples grouped into six Java packages.

## Requirements
- JDK 17 or newer
- Eclipse IDE for Java Developers (optional, but recommended)

No external Java libraries are required.

## Open in Eclipse
1. Extract `LegalSearch.zip`.
2. Open Eclipse.
3. Choose **File → Import → General → Existing Projects into Workspace**.
4. Select the extracted `LegalSearch` folder and click **Finish**.
5. In Package Explorer, expand `src`.
6. Right-click `legalsearch.Main.java` → **Run As → Java Application** for the console menu.
7. To launch the browser app instead, right-click `legalsearch.web.WebServer.java` → **Run As → Java Application**, then open `http://localhost:8080`.

If Eclipse asks you to select a JRE, select a JDK 17+ installation.

## Run from macOS Terminal / Linux
From the extracted `LegalSearch` directory:

```bash
javac -d bin $(find src -name "*.java")
java -cp bin legalsearch.Main
```

To run the website, use a separate terminal window:

```bash
java -cp bin legalsearch.web.WebServer
```

Then visit `http://localhost:8080`. Stop the server with `Ctrl+C`.

## Data files
CSV data lives in `data/`. The app uses paths relative to the project folder, so run it from the extracted `LegalSearch` folder. The sample dataset is fictional and for demonstration only. You can edit the CSV files with a spreadsheet editor or text editor. Avoid running multiple app instances that write the same CSV files at the same time.

## Features
- Search documents with KMP and a multi-term Aho-Corasick example.
- Compare two document texts with edit distance.
- Suggest query corrections using Levenshtein distance.
- Assign available lawyers to cases with a simple bipartite matching demo.
- Run examples for the algorithms from Modules 1–6.
- Add a case record through the website; search history is saved in CSV.

## Scope note
This is an educational starter project, not legal advice or a production legal case-management system. The SAT/3-SAT, reductions, suffix structures, parallel scan/reduce, randomized algorithms, and other advanced topics are represented as small runnable algorithm demonstrations; some are simplified teaching implementations rather than production-scale implementations.
