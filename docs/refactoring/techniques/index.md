# Refactoring Techniques Index

Dưới đây là danh sách đầy đủ các kỹ thuật Refactoring từ **Refactoring Guru**. Mỗi kỹ thuật đều có tài liệu chi tiết về Vấn đề, Giải pháp và Lý do thực hiện.

## Categories

<a name="composing-methods"></a>
### Composing Methods
Streamlining methods, removing code duplication, and making logic easier to understand.
- [Extract Method](composing-methods/extract-method.md)
- [Inline Method](composing-methods/inline-method.md)
- [Extract Variable](composing-methods/extract-variable.md)
- [Inline Temp](composing-methods/inline-temp.md)
- [Replace Temp with Query](composing-methods/replace-temp-with-query.md)
- [Split Temporary Variable](composing-methods/split-temporary-variable.md)
- [Remove Assignments to Parameters](composing-methods/remove-assignments-to-parameters.md)
- [Replace Method with Method Object](composing-methods/replace-method-with-method-object.md)
- [Substitute Algorithm](composing-methods/substitute-algorithm.md)

<a name="moving-features-between-objects"></a>
### Moving Features between Objects
Safely moving functionality between classes and hiding implementation details.
- [Move Method](moving-features/move-method.md)
- [Move Field](moving-features/move-field.md)
- [Extract Class](moving-features/extract-class.md)
- [Inline Class](moving-features/inline-class.md)
- [Hide Delegate](moving-features/hide-delegate.md)
- [Remove Middle Man](moving-features/remove-middle-man.md)
- [Introduce Foreign Method](moving-features/introduce-foreign-method.md)
- [Introduce Local Extension](moving-features/introduce-local-extension.md)

<a name="organizing-data"></a>
### Organizing Data
Handling data and replacing primitives with objects.
- [Self Encapsulate Field](organizing-data/self-encapsulate-field.md)
- [Replace Data Value with Object](organizing-data/replace-data-value-with-object.md)
- [Change Value to Reference](organizing-data/change-value-to-reference.md)
- [Change Reference to Value](organizing-data/change-reference-to-value.md)
- [Replace Array with Object](organizing-data/replace-array-with-object.md)
- [Duplicate Observed Data](organizing-data/duplicate-observed-data.md)
- [Change Unidirectional Association to Bidirectional](organizing-data/change-unidirectional-association-to-bidirectional.md)
- [Change Bidirectional Association to Unidirectional](organizing-data/change-bidirectional-association-to-unidirectional.md)
- [Replace Magic Number with Symbolic Constant](organizing-data/replace-magic-number-with-symbolic-constant.md)
- [Encapsulate Field](organizing-data/encapsulate-field.md)
- [Encapsulate Collection](organizing-data/encapsulate-collection.md)
- [Replace Type Code with Class](organizing-data/replace-type-code-with-class.md)
- [Replace Type Code with Subclasses](organizing-data/replace-type-code-with-subclasses.md)
- [Replace Type Code with State/Strategy](organizing-data/replace-type-code-with-state-strategy.md)
- [Replace Subclass with Fields](organizing-data/replace-subclass-with-fields.md)

<a name="simplifying-conditional-expressions"></a>
### Simplifying Conditional Expressions
Combating the complexity of conditional logic.
- [Decompose Conditional](simplifying-conditionals/decompose-conditional.md)
- [Consolidate Conditional Expression](simplifying-conditionals/consolidate-conditional-expression.md)
- [Consolidate Duplicate Conditional Fragments](simplifying-conditionals/consolidate-duplicate-conditional-fragments.md)
- [Remove Control Flag](simplifying-conditionals/remove-control-flag.md)
- [Replace Nested Conditional with Guard Clauses](simplifying-conditionals/replace-nested-conditional-with-guard-clauses.md)
- [Replace Conditional with Polymorphism](simplifying-conditionals/replace-conditional-with-polymorphism.md)
- [Introduce Null Object](simplifying-conditionals/introduce-null-object.md)
- [Introduce Assertion](simplifying-conditionals/introduce-assertion.md)

<a name="simplifying-method-calls"></a>
### Simplifying Method Calls
Making method calls simpler and interfaces easier to understand.
- [Rename Method](simplifying-method-calls/rename-method.md)
- [Add Parameter](simplifying-method-calls/add-parameter.md)
- [Remove Parameter](simplifying-method-calls/remove-parameter.md)
- [Separate Query from Modifier](simplifying-method-calls/separate-query-from-modifier.md)
- [Parameterize Method](simplifying-method-calls/parameterize-method.md)
- [Replace Parameter with Explicit Methods](simplifying-method-calls/replace-parameter-with-explicit-methods.md)
- [Preserve Whole Object](simplifying-method-calls/preserve-whole-object.md)
- [Replace Parameter with Method Call](simplifying-method-calls/replace-parameter-with-method-call.md)
- [Introduce Parameter Object](simplifying-method-calls/introduce-parameter-object.md)
- [Remove Setting Method](simplifying-method-calls/remove-setting-method.md)
- [Hide Method](simplifying-method-calls/hide-method.md)
- [Replace Constructor with Factory Method](simplifying-method-calls/replace-constructor-with-factory-method.md)
- [Replace Error Code with Exception](simplifying-method-calls/replace-error-code-with-exception.md)
- [Replace Exception with Test](simplifying-method-calls/replace-exception-with-test.md)

<a name="dealing-with-generalization"></a>
### Dealing with Generalization
Managing inheritance and abstraction.
- [Pull Up Field](dealing-with-generalization/pull-up-field.md)
- [Pull Up Method](dealing-with-generalization/pull-up-method.md)
- [Pull Up Constructor Body](dealing-with-generalization/pull-up-constructor-body.md)
- [Push Down Method](dealing-with-generalization/push-down-method.md)
- [Push Down Field](dealing-with-generalization/push-down-field.md)
- [Extract Subclass](dealing-with-generalization/extract-subclass.md)
- [Extract Superclass](dealing-with-generalization/extract-super-class.md)
- [Extract Interface](dealing-with-generalization/extract-interface.md)
- [Collapse Hierarchy](dealing-with-generalization/collapse-hierarchy.md)
- [Form Template Method](dealing-with-generalization/form-template-method.md)
- [Replace Inheritance with Delegation](dealing-with-generalization/replace-inheritance-with-delegation.md)
- [Replace Delegation with Inheritance](dealing-with-generalization/replace-delegation-with-inheritance.md)
