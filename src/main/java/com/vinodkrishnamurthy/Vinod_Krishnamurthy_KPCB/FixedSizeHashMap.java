package vinodKPCBChallenge;

/**
 * Here is my implementation of a fixed-size hash map.
 *
 * The hash map is an array of Nodes. Each node is the root node of an AVL binary
 * search tree. This implementation is faster than the traditional implementation
 * with linked lists because binary search trees will have a runtime of O(log n)
 * for search, insertion, and deletion operations when collisions occur. The hash map
 * is designed to support those functions. Since this implementation is of a
 * fixed-size hash map, collisions will occur quite often. Generally, hash maps have
 * a default load factor of 0.75 at which they double in size, so a fixed size 
 * hash map must focus on efficiently accounting for collisions.
 */

public class FixedSizeHashMap<T> {
    
    //NODE CLASS
    /**
     * A private class that defines a Node.
     */
    private final class Node<T> {
        //INSTANCE VARIABLES
     
        // The key held by this node
        private int key;

        // The value held by this node
        private T value;

        // The height of this node
        private int height;

        // The index of this node's left child

        private int left;

        // The index of this node's right child
        private int right;

        //CONSTRUCTOR
        /**
         * Creates an empty node, with all fields set to either -1 or null.
         */
        private Node() {
            this.reset();
        }

        //METHODS
        /**
         * Resets all fields of this node to their original value (-1 or null)
         */
        private void reset() {
            this.key = -1;
            this.value = null;
            this.height = -1;
            this.left = -1;
            this.right = -1;
        }
    }

    //INSTANCE VARIABLES
    /**
     * The array that holds all of this hash map's nodes
     */
    private Node[] tree;
    
    /**
     * The byte array used to mark active nodes
     */
    private byte[] bitmap;
    
    /**
     * The index of the root node of the hash map
     */
    private int root_index;
    
    /**
     * The index of the node most recently deleted from the tree
     */
    private int delete_index;
    
    /**
     * The size of the hash map
     */
    private final int size;
    
    /**
     * The number of elements in the hash map
     */
    private int elements;

    //CONSTRUCTOR
    /**
     * Creates an instance of a fixed-size hash map
     * The root index is set to -1 and the number of elements is set to 0
     */
    public FixedSizeHashMap(int size) {
        if (size > 0) {
            this.tree = new Node[size];
            for (int i = 0; i < size; i++) this.tree[i] = new Node();
            this.bitmap = new byte[size/8 + 1];
            this.root_index = -1;
            this.delete_index = -1;
            this.size = size;
            this.elements = 0;
        } else {
            throw new IllegalArgumentException("The size of the hash map must be a positive integer.");
        }
    }

    //USER METHODS
    /**
     * Associates given key to a given value in this hash map.
     * Returns a boolean indicating the success/failure of the operation.
     */
    public boolean set(String key, T value) {
        if (this.elements < this.size && value != null) {
            int new_index = this.getAvailableNode();
            this.tree[new_index].key = key.hashCode();
            this.tree[new_index].value = value;
            this.tree[new_index].height = 0;
            try {
                //throws an IllegalArgumentException if the key is already used
                this.root_index = this.insert(new_index, this.root_index);
                this.bitFlip(new_index);
                this.elements++;
                return true;
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
                System.out.printf("%d    %d", new_index, this.root_index);
                return false;
            } catch (IllegalArgumentException e) {
                //reset node and return false
                this.tree[new_index].reset();
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Returns the value associated with a given key.
     */
    public T get(String key) {
        if (this.elements > 0 && this.root_index != -1) {
            //get the index of the node with the given string
            int node_index = this.find(key.hashCode(), this.root_index);
            return (node_index != -1) ? (T) this.tree[node_index].value : null;
        } else {
            return null;
        }
    }

    /**
     * Deletes the entry with the given key from this hash map
     *
     * Also returns the keys associated value
     */
    public T delete(String key) {
        if (this.elements > 0 && this.root_index != -1) {
            //attempt to remove the node with key from the implicit tree
            this.root_index = this.remove(key.hashCode(), this.root_index);
            //this.delete_index will hold the index of the node that should be delted
            if (this.delete_index != -1) {
                //save the return value
                T node_String = (T) this.tree[delete_index].value;
                //clean the deleted node and mark as inactive
                this.tree[this.delete_index].reset();
                this.bitFlip(this.delete_index);
                this.delete_index = -1;
                this.elements--;
                return node_String;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Returns the load of this fixed-size hash map
     */
    public float load() {
        return (float)this.elements/this.size;
    }

    /**
     * Returns the size of the hashmap
     */
    public int getSize() {
        return this.size;
    }

    //TREE UTILITIES
    /**
     * Traverses the subtree that is rooted by start_index and inserts a new node at
     * the specified new index
     */
    private int insert(int new_index, int start_index) throws IllegalArgumentException {
        //insert at start_index if start_index isn't actually part of the implicit tree
        if (start_index == -1) {
            return new_index;
        }
        //insert into left subtree
        else if (this.tree[new_index].key < this.tree[start_index].key) {
            this.tree[start_index].left = this.insert(new_index, this.tree[start_index].left);
        }
        //insert into right subtree
        else if (this.tree[new_index].key > this.tree[start_index].key) {
            this.tree[start_index].right = this.insert(new_index, this.tree[start_index].right);
        }
        //duplicate key
        else {
            throw new IllegalArgumentException("Key already used.");
        }
        //rebalance tree and return
        return this.rebalance(start_index);
    }
    
    /**
     * Returns the index of the node with the given key
     */
    private int find(int key, int start_index) {
        if (start_index == -1) {
            return -1;
        } else if (key == this.tree[start_index].key) {
            return start_index;
        } else if (key < this.tree[start_index].key) {
            return this.find(key, this.tree[start_index].left);
        } else {
            return this.find(key, this.tree[start_index].right);
        }
    }

    /**
     * Recursive method that traverses the tree and removes the node associated with the given key
     */
    private int remove(int key, int start_index) {
        //start_index isn't actually part of the implicit tree
        if (start_index == -1) {
            this.delete_index = -1;
            return -1;
        }
        //remove from left subtree
        else if (key < this.tree[start_index].key) {
            this.tree[start_index].left = this.remove(key, this.tree[start_index].left);
            return this.rebalance(start_index);
        }
        //remove from right subtree
        else if (key > this.tree[start_index].key) {
            this.tree[start_index].right = this.remove(key, this.tree[start_index].right);
            return this.rebalance(start_index);
        }
        //start_index is the node to be removed
        else {
            int left_index = this.tree[start_index].left;
            int right_index = this.tree[start_index].right;
            this.delete_index = start_index;
            //node is a leaf, simply remove it
            if (left_index == -1 && right_index == -1) {
                return -1;
            }
            //node has a single child, give the node it's grandchildren
            else if (left_index != -1 && right_index == -1) {
                return left_index;
            } else if (left_index == -1 && right_index != -1) {
                return right_index;
            }
            //node has two children, replace the node's successor (smallest
            //node in right subtree), then remove the successor
            else {
                int smallest_index = this.getSmallest(right_index);
                int temp_index = start_index;
                this.nodeSwap(start_index, smallest_index);
                this.tree[start_index].right = this.remove(this.tree[smallest_index].key, right_index);
                this.delete_index = smallest_index;
                return this.rebalance(start_index);
            }
        }
    }

    /**
     * Balances the AVL subtree using LL, LR, RL, and RR balance factors
     */
    private int rebalance(int start_index) {
        if (start_index == -1) System.out.println("start index = -1.");
        int newstart_index;
        int left_index = this.tree[start_index].left;
        int right_index = this.tree[start_index].right;
        //left subtree heavy
        if (this.balanceFactor(start_index) == 2) {
            if (this.tree[left_index].left == -1) {
                newstart_index = this.rotateCaseLL(start_index);
            } else {
                newstart_index = this.rotateCaseLR(start_index);
            }
        }
        //right subtree heavy
        else if (this.balanceFactor(start_index) == -2) {
            if (this.tree[right_index].right == -1) {
                newstart_index = this.rotateCaseRL(start_index);
            } else {
                newstart_index = this.rotateCaseRR(start_index);
            }
        }
        //no rebalancing needed
        else {
            newstart_index = start_index;
        }
        //update height if necessary
        this.updateHeight(start_index);
        return newstart_index;
    }

    /**
     * Returns the index of the smallest node in the subtree rooted by start_index
     */
    private int getSmallest(int start_index) {
        if (this.tree[start_index].left != -1) {
            return this.getSmallest(this.tree[start_index].left);
        } else {
            return start_index;
        }
    }

    /**
     * Swaps the keys and values of the nodes at indices a and b
     */
    private void nodeSwap(int a, int b) {
        int tempKey = this.tree[a].key;
        this.tree[a].key = this.tree[b].key;
        this.tree[b].key = tempKey;
        T tempValue = (T) this.tree[a].value;
        this.tree[a].value = this.tree[b].value;
        this.tree[b].value = tempValue;
    }

    /**
     * Returns the balance factor (difference between node's left 
     * and right subtrees) of the subtree rooted by the node at index i
     */
    private int balanceFactor(int i) {
        return this.height(this.tree[i].left) - this.height(this.tree[i].right);
    }

    /**
     * Returns the height of the node at index i
     */
    private int height(int i) {
        return (i != -1) ? this.tree[i].height : -1;
    }

    /**
     * Updates the height of the node at index i
     */
    private void updateHeight(int i) {
        if (i != -1) {
            int left_index = this.tree[i].left;
            int right_index = this.tree[i].right;
            if (left_index == -1 && right_index == -1)
                this.tree[i].height = 0;
            else if (left_index != -1 && right_index == -1)
                this.tree[i].height = this.tree[left_index].height + 1;
            else if (left_index == -1 && right_index != -1)
                this.tree[i].height = this.tree[right_index].height + 1;
            else
                this.tree[i].height = this.max(this.height(left_index), this.height(right_index)) + 1;
        }
    }

    //TREE ROTATIONS (PRIVATE)
    /**
     * left left rotation case
     */
    private int rotateCaseLL(int start_index) {
        int newstart_index = this.tree[start_index].left;
        if (newstart_index == -1) {
            return start_index;
        } else {
            this.tree[start_index].left = this.tree[newstart_index].right;
            this.tree[newstart_index].right = start_index;
            //update heights
            this.updateHeight(start_index);
            this.updateHeight(newstart_index);
            return newstart_index;
        }
    }

    /**
     * right right rotation case
     */
    private int rotateCaseRR(int start_index) {
        int newstart_index = this.tree[start_index].right;
        if (newstart_index == -1) {
            return start_index;
        } else {
            this.tree[start_index].right = this.tree[newstart_index].left;
            this.tree[newstart_index].left = start_index;
            //update heights
            this.updateHeight(start_index);
            this.updateHeight(newstart_index);
            return newstart_index;
        }
    }

    /**
     * left right rotation case
     */
    private int rotateCaseLR(int start_index) {
        this.tree[start_index].left = this.rotateCaseRR(this.tree[start_index].left);
        return this.rotateCaseLL(start_index);
    }

    /**
     * right left rotation case
     */
    private int rotateCaseRL(int start_index) {
        this.tree[start_index].right = this.rotateCaseLL(this.tree[start_index].right);
        return this.rotateCaseRR(start_index);
    }

    //BITMAP UTILITIES
    /**
     * Returns the index of the first available node in the internal array.
     */
    private int getAvailableNode() {
        int i = 0;
        for (; this.bitmap[i] == -1; i++);
        int j = 0;
        for (; j < 8; j++) {
            //break out of loop the first time a 0 is encountered
            if ((this.bitmap[i] & (1 << j)) == 0) {
                break;
            }
        }
        //ensure the returned value is less than the max size of this hashmap
        return (8*i + j < this.size) ? 8*i + j : -1;
    }

    /**
     * Flips the xth bit in the hash map's internal bitmap
     */
    private void bitFlip(int x) {
        int index = x/8;
        int offset = x%8;
        this.bitmap[index] ^= (1 << offset);
    }

    //MISC UTILITIES
    /**
     * Returns the max of two integers
     */
    private static int max(int a, int b) {
        return (a > b) ? a : b;
    }
}
