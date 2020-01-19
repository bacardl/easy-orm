package com.softserve.easy.meta;

import com.softserve.easy.entity.*;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;

class DirectedGraphTest {
    @Test
    void directedGraphShouldContainFiveVertexAndZeroEdges() {
        DependencyGraph.DirectedGraph<Class<?>> graph = new DependencyGraph.DirectedGraph<>();
        graph.addVertex(User.class);
        graph.addVertex(Person.class);
        graph.addVertex(Country.class);
        graph.addVertex(Product.class);
        graph.addVertex(Order.class);
        assertThat(graph.getAdjVertices().values(), Matchers.hasSize(5));
        graph.getAdjVertices().values().forEach(list -> assertThat(list, Matchers.empty()));
    }

    @Test
    void directedGraphShouldContainTwoVertexAndOneEdges() {
        DependencyGraph.DirectedGraph<Class<?>> graph = new DependencyGraph.DirectedGraph<>();
        graph.addVertex(User.class);
        graph.addVertex(Person.class);
        graph.addEdge(User.class, Person.class);
        assertThat(graph.getAdjVertices().size(), Matchers.is(2));
        Optional<Integer> numberOfEdges = graph.getAdjVertices().values()
                .stream().map(List::size).reduce(Integer::sum);
        assertThat(numberOfEdges.get(), Matchers.is(1));
    }

    @Test
    void layeredDependencyTest() {
        DependencyGraph.DirectedGraph<Integer> graph = new DependencyGraph.DirectedGraph<>();
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addVertex(4);
        graph.addVertex(5);
        graph.addVertex(6);
        graph.addVertex(7);
        graph.addEdge(1,6);
        graph.addEdge(1,2);
        graph.addEdge(6,2);
        graph.addEdge(6,7);
        graph.addEdge(2,3);
        graph.addEdge(3,4);
        graph.addEdge(3,5);
        graph.addEdge(5,1);

        List<Set<DependencyGraph.DependencyPair<Integer>>> layeredDependencies =
                graph.layeredBreadthFirstTraversal(1);
        for (Set<DependencyGraph.DependencyPair<Integer>> layer : layeredDependencies) {
            System.out.println("\t\t layer");
            for (DependencyGraph.DependencyPair<Integer> classDependencyPair : layer) {
                System.out.println(classDependencyPair.toString());
            }
            System.out.println("\t\t /layer");
        }

    }


}