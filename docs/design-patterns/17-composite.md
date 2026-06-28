# 17-composite

Source: https://refactoring.guru/design-patterns/composite

/ [Design Patterns](/design-patterns)
/ [Structural Patterns](/design-patterns/structural-patterns)

# Composite

Also known as: Object Tree

## Intent

**Composite** is a structural design pattern that lets you compose objects into tree structures and then work with these structures as if they were individual objects.

![Composite design pattern](/images/patterns/content/composite/composite.png?id=73bcf0d94db360b636cd745f710d19db)

## Problem

Using the Composite pattern makes sense only when the core model of your app can be represented as a tree.

For example, imagine that you have two types of objects: `Products` and `Boxes`. A `Box` can contain several `Products` as well as a number of smaller `Boxes`. These little `Boxes` can also hold some `Products` or even smaller `Boxes`, and so on.

Say you decide to create an ordering system that uses these classes. Orders could contain simple products without any wrapping, as well as boxes stuffed with products...and other boxes. How would you determine the total price of such an order?

![Structure of a complex order](/images/patterns/diagrams/composite/problem-en.png?id=3320d7ddc5bdc3e43752bb4393710794)

An order might comprise various products, packaged in boxes, which are packaged in bigger boxes and so on. The whole structure looks like an upside down tree.

You could try the direct approach: unwrap all the boxes, go over all the products and then calculate the total. That would be doable in the real world; but in a program, it’s not as simple as running a loop. You have to know the classes of `Products` and `Boxes` you’re going through, the nesting level of the boxes and other nasty details beforehand. All of this makes the direct approach either too awkward or even impossible.

## Solution

The Composite pattern suggests that you work with `Products` and `Boxes` through a common interface which declares a method for calculating the total price.

How would this method work? For a product, it’d simply return the product’s price. For a box, it’d go over each item the box contains, ask its price and then return a total for this box. If one of these items were a smaller box, that box would also start going over its contents and so on, until the prices of all inner components were calculated. A box could even add some extra cost to the final price, such as packaging cost.

![Solution suggested by the Composite pattern](/images/patterns/content/composite/composite-comic-1-en.png?id=5510969e5584e7c0cb65d533901bb8f6)

The Composite pattern lets you run a behavior recursively over all components of an object tree.

The greatest benefit of this approach is that you don’t need to care about the concrete classes of objects that compose the tree. You don’t need to know whether an object is a simple product or a sophisticated box. You can treat them all the same via the common interface. When you call a method, the objects themselves pass the request down the tree.

## Real-World Analogy

![An example of a military structure](/images/patterns/diagrams/composite/live-example.png?id=548a7cec45b493af66e8bfe524a137d3)

An example of a military structure.

Armies of most countries are structured as hierarchies. An army consists of several divisions; a division is a set of brigades, and a brigade consists of platoons, which can be broken down into squads. Finally, a squad is a small group of real soldiers. Orders are given at the top of the hierarchy and passed down onto each level until every soldier knows what needs to be done.

## Structure

![Structure of the Composite design pattern](/images/patterns/diagrams/composite/structure-en.png?id=b7f114558b594dfb220d225398b2b744)![Structure of the Composite design pattern](/images/patterns/diagrams/composite/structure-en-indexed.png?id=9fbce571969f4bc9bb57ee4a7d786852)

1. The **Component** interface describes operations that are common to both simple and complex elements of the tree.
2. The **Leaf** is a basic element of a tree that doesn’t have sub-elements.

   Usually, leaf components end up doing most of the real work, since they don’t have anyone to delegate the work to.
3. The **Container** (aka *composite*) is an element that has sub-elements: leaves or other containers. A container doesn’t know the concrete classes of its children. It works with all sub-elements only via the component interface.

   Upon receiving a request, a container delegates the work to its sub-elements, processes intermediate results and then returns the final result to the client.
4. The **Client** works with all elements through the component interface. As a result, the client can work in the same way with both simple or complex elements of the tree.

## Pseudocode

In this example, the **Composite** pattern lets you implement stacking of geometric shapes in a graphical editor.

![Structure of the Composite example](/images/patterns/diagrams/composite/example.png?id=98ba81d07c979038dd2ebf3c83a2e19f)

The geometric shapes editor example.

The `CompoundGraphic` class is a container that can comprise any number of sub-shapes, including other compound shapes. A compound shape has the same methods as a simple shape. However, instead of doing something on its own, a compound shape passes the request recursively to all its children and “sums up” the result.

The client code works with all shapes through the single interface common to all shape classes. Thus, the client doesn’t know whether it’s working with a simple shape or a compound one. The client can work with very complex object structures without being coupled to concrete classes that form that structure.

```
// The component interface declares common operations for both
// simple and complex objects of a composition.
interface Graphic is
    method move(x, y)
    method draw()

// The leaf class represents end objects of a composition. A
// leaf object can't have any sub-objects. Usually, it's leaf
// objects that do the actual work, while composite objects only
// delegate to their sub-components.
class Dot implements Graphic is
    field x, y

    constructor Dot(x, y) { ... }

    method move(x, y) is
        this.x += x, this.y += y

    method draw() is
        // Draw a dot at X and Y.

// All component classes can extend other components.
class Circle extends Dot is
    field radius

    constructor Circle(x, y, radius) { ... }

    method draw() is
        // Draw a circle at X and Y with radius R.

// The composite class represents complex components that may
// have children. Composite objects usually delegate the actual
// work to their children and then "sum up" the result.
class CompoundGraphic implements Graphic is
    field children: array of Graphic

    // A composite object can add or remove other components
    // (both simple or complex) to or from its child list.
    method add(child: Graphic) is
        // Add a child to the array of children.

    method remove(child: Graphic) is
        // Remove a child from the array of children.

    method move(x, y) is
        foreach (child in children) do
            child.move(x, y)

    // A composite executes its primary logic in a particular
    // way. It traverses recursively through all its children,
    // collecting and summing up their results. Since the
    // composite's children pass these calls to their own
    // children and so forth, the whole object tree is traversed
    // as a result.
    method draw() is
        // 1. For each child component:
        //     - Draw the component.
        //     - Update the bounding rectangle.
        // 2. Draw a dashed rectangle using the bounding
        // coordinates.


// The client code works with all the components via their base
// interface. This way the client code can support simple leaf
// components as well as complex composites.
class ImageEditor is
    field all: CompoundGraphic

    method load() is
        all = new CompoundGraphic()
        all.add(new Dot(1, 2))
        all.add(new Circle(5, 3, 10))
        // ...

    // Combine selected components into one complex composite
    // component.
    method groupSelected(components: array of Graphic) is
        group = new CompoundGraphic()
        foreach (component in components) do
            group.add(component)
            all.remove(component)
        all.add(group)
        // All components will be drawn.
        all.draw()
```

## Applicability

Use the Composite pattern when you have to implement a tree-like object structure.

The Composite pattern provides you with two basic element types that share a common interface: simple leaves and complex containers. A container can be composed of both leaves and other containers. This lets you construct a nested recursive object structure that resembles a tree.

Use the pattern when you want the client code to treat both simple and complex elements uniformly.

All elements defined by the Composite pattern share a common interface. Using this interface, the client doesn’t have to worry about the concrete class of the objects it works with.

## How to Implement

1. Make sure that the core model of your app can be represented as a tree structure. Try to break it down into simple elements and containers. Remember that containers must be able to contain both simple elements and other containers.
2. Declare the component interface with a list of methods that make sense for both simple and complex components.
3. Create a leaf class to represent simple elements. A program may have multiple different leaf classes.
4. Create a container class to represent complex elements. In this class, provide an array field for storing references to sub-elements. The array must be able to store both leaves and containers, so make sure it’s declared with the component interface type.

   While implementing the methods of the component interface, remember that a container is supposed to be delegating most of the work to sub-elements.
5. Finally, define the methods for adding and removal of child elements in the container.

   Keep in mind that these operations can be declared in the component interface. This would violate the *Interface Segregation Principle* because the methods will be empty in the leaf class. However, the client will be able to treat all the elements equally, even when composing the tree.

## Pros and Cons

* You can work with complex tree structures more conveniently: use polymorphism and recursion to your advantage.
* *Open/Closed Principle*. You can introduce new element types into the app without breaking the existing code, which now works with the object tree.

* It might be difficult to provide a common interface for classes whose functionality differs too much. In certain scenarios, you’d need to overgeneralize the component interface, making it harder to comprehend.

## Relations with Other Patterns

* You can use [Builder](/design-patterns/builder) when creating complex [Composite](/design-patterns/composite) trees because you can program its construction steps to work recursively.
* [Chain of Responsibility](/design-patterns/chain-of-responsibility) is often used in conjunction with [Composite](/design-patterns/composite). In this case, when a leaf component gets a request, it may pass it through the chain of all of the parent components down to the root of the object tree.
* You can use [Iterators](/design-patterns/iterator) to traverse [Composite](/design-patterns/composite) trees.
* You can use [Visitor](/design-patterns/visitor) to execute an operation over an entire [Composite](/design-patterns/composite) tree.
* You can implement shared leaf nodes of the [Composite](/design-patterns/composite) tree as [Flyweights](/design-patterns/flyweight) to save some RAM.
* [Composite](/design-patterns/composite) and [Decorator](/design-patterns/decorator) have similar structure diagrams since both rely on recursive composition to organize an open-ended number of objects.

  A *Decorator* is like a *Composite* but only has one child component. There’s another significant difference: *Decorator* adds additional responsibilities to the wrapped object, while *Composite* just “sums up” its children’s results.

  However, the patterns can also cooperate: you can use *Decorator* to extend the behavior of a specific object in the *Composite* tree.
* Designs that make heavy use of [Composite](/design-patterns/composite) and [Decorator](/design-patterns/decorator) can often benefit from using [Prototype](/design-patterns/prototype). Applying the pattern lets you clone complex structures instead of re-constructing them from scratch.

## Code Examples

[![Composite in C#](/images/patterns/icons/csharp.svg?id=da64592defc6e86d57c39c66e9de3e58)](/design-patterns/composite/csharp/example "Composite in C#")
[![Composite in C++](/images/patterns/icons/cpp.svg?id=f7782ed8b8666246bfcc3f8fefc3b858)](/design-patterns/composite/cpp/example "Composite in C++")
[![Composite in Go](/images/patterns/icons/go.svg?id=1a89927eb99b1ea3fde7701d97970aca)](/design-patterns/composite/go/example "Composite in Go")
[![Composite in Java](/images/patterns/icons/java.svg?id=e6d87e2dca08c953fe3acd1275ed4f4e)](/design-patterns/composite/java/example "Composite in Java")
[![Composite in PHP](/images/patterns/icons/php.svg?id=be1906eb26b71ec1d3b93720d6156618)](/design-patterns/composite/php/example "Composite in PHP")
[![Composite in Python](/images/patterns/icons/python.svg?id=6d815d43c0f7050a1151b43e51569c9f)](/design-patterns/composite/python/example "Composite in Python")
[![Composite in Ruby](/images/patterns/icons/ruby.svg?id=b065b718c914bf8e960ef731600be1eb)](/design-patterns/composite/ruby/example "Composite in Ruby")
[![Composite in Rust](/images/patterns/icons/rust.svg?id=1f5698a4b5ae23fe79413511747e4a87)](/design-patterns/composite/rust/example "Composite in Rust")
[![Composite in Swift](/images/patterns/icons/swift.svg?id=0b716c2d52ec3a48fbe91ac031070c1d)](/design-patterns/composite/swift/example "Composite in Swift")
[![Composite in TypeScript](/images/patterns/icons/typescript.svg?id=2239d0f16cb703540c205dd8cb0c0cb7)](/design-patterns/composite/typescript/example "Composite in TypeScript")

[![](/images/patterns/banners/patterns-book-banner-3.png?id=7d445df13c80287beaab234b4f3b698c)](/design-patterns/book)

### Support our free website and own the eBook!

* 22 design patterns and 8 principles explained in depth.
* 409 well-structured, easy to read, jargon-free pages.
* 225 clear and helpful illustrations and diagrams.
* An archive with code examples in 11 languages.
* All devices supported: PDF/EPUB/MOBI/KFX formats.

 [Learn more…](/design-patterns/book)

[![](/images/patterns/book/web-cover-en.png?id=328861769fd11617674e3b8a7e2dd9e7)](/design-patterns/book)

This article is a part of our eBook  
**Dive Into Design Patterns**.

 [Learn more…](/design-patterns/book)