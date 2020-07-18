# printscript
Typescript-ish language interpreter

## Implemented Features
The following features have been implemented:

#### Lexer
- Token recognition storing value, type, line and column range
- Support for modifiable lex analysis rules
    - Consider "boolean" as VAR_TYPE
    - Consider "true" or "false" as BOOLEAN
    - Consider "const" as ASSIGN_TYPE
    - Consider "<", "<=", ">=", ">", "==", "!=" as OPERATORS
- Prints token list once job is done

#### Parser
- Supports the following statements:
    - Variable Creation (ending in ; or with expression)
    - Variable Reassign
    - If and If Else
    - File Import
    - Function Call
- Expressions as binary tree with BOOLEAN/STRING/NUMBER/IDENTIFIER as leafs
- ParserExceptions are used for unexpected tokens
- Prints node tree once job is done

#### Executor
- Supports the following modes:
    - Validation
    - Interpretation
- ExecutorException are used for type mismatching, undefined variables, unmutable variables, etc
- Prints execution mode results as it traverses the tree

#### CLI
- Allows user input for the following:
    - File path
    - BOOLEAN type support
    - boolean OPERATORS support
    - const ASSIGN_TYPE support
    - Execution mode
