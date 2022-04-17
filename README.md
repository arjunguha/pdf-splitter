# Usage Example

Save this file as `main.tex`

```latex
\documentclass{article}

\usepackage{hyperref}

\begin{document}

\section{Document 1}
\pdfbookmark[0]{file:file1}{file:file1}

This is Document 1

\newpage

\section{Document 2}
\pdfbookmark[0]{file:file2}{file:file2}

This is Document 2

\newpage

\section{End of Document}
\pdfbookmark[0]{file:bogus}{file:bogus}

This is necessary to avoid a bug. The program fails to create the last document.

\end{document}
```

Run `pdf-splitter`:

```bash
$ pdflatex main.tex
$ mkdir out
$ pdf-splitter main.pdf out/
$ ls out/
file1.pdf file2.pdf
```

Ensure that each `\pdfbookmark` is on a new page. If you use `\chapter`, that takes care of it.

