import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.Collectors;

/**
 * Construct a BST from given pre-order traversal order
 */

final class ConstructFromPreOrder {
    private static class Node {
        private int value;
        private Node left;
        private Node right;

        private Node(int value) {
            this.value = value;
        }

        private static Node constructFromPreOrder(int[] preOrder) {
            if (preOrder == null || preOrder.length == 0) {
                return null;
            }

            Node root = new Node(preOrder[0]);
            int leftSubTreePortion = 0;
            while (leftSubTreePortion < preOrder.length
                    && preOrder[leftSubTreePortion++] <= root.value);
            if (leftSubTreePortion > 0) {
                int[] leftPreOrder = Arrays.copyOfRange(preOrder, 1, leftSubTreePortion);
                root.left = constructFromPreOrder(leftPreOrder);
            }

            if (leftSubTreePortion < preOrder.length) {
                int[] rightPreOrder =
                    Arrays.copyOfRange(preOrder, leftSubTreePortion, preOrder.length);
                root.right = constructFromPreOrder(rightPreOrder);
            }

            return root;
        }

        private static Integer[] getPreOrder(Node root) {
            if (root == null) {
                return null;
            }

            List<Integer> preOrderList = new ArrayList<>();
            preOrderList.add(root.value);
            
            Integer[] leftPreOrder = getPreOrder(root.left);
            if (leftPreOrder != null) {
                preOrderList.addAll(Arrays.asList(leftPreOrder));
            }

            Integer[] rightPreOrder = getPreOrder(root.right);
            if (rightPreOrder != null) {
                preOrderList.addAll(Arrays.asList(rightPreOrder));
            }

            return preOrderList.toArray(new Integer[preOrderList.size()]);
        }

    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please provide a non-empty list of integer values in pre-order traversal order for a binary search tree.");
            return;
        }

        int[] preOrder = Stream.of(args[0].split(",")).mapToInt(Integer::parseInt).toArray();
        Node tree = Node.constructFromPreOrder(preOrder);
        Integer[] regeneratedPreOrder = Node.getPreOrder(tree);
        System.out.println(
            String.join(",", Stream.of(regeneratedPreOrder).map(String::valueOf).collect(Collectors.toList())));
    }
}

