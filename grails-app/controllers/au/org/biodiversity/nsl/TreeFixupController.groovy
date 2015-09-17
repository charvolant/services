package au.org.biodiversity.nsl

import au.org.biodiversity.nsl.tree.ClassificationManagerService

/**
 * This controller allows the user to manually perform versioning on nodes.
 *
 * We store the state in a session variable, mainly because it's just plain easier.
 *
 * The entry points are methods that pre-load the session variable with a selection of nodes
 * chosen as a result of certain situations that may occur.
 *
 */
class TreeFixupController {
    private static String SELECTED_NODES_KEY = TreeFixupController.class.getName() + '#selectedNodes';
    ClassificationManagerService classificationManagerService;


    def selectNameNode() {
        def state = []
        session[SELECTED_NODES_KEY] = state;

        Arrangement c = Arrangement.findByLabelAndArrangementType(params['classification'], ArrangementType.P)
        Name n = Name.get(params['nameId'])

       [
               classification: c,
               name: n,
               nodes:  Node.findAllByRootAndNameAndReplacedAt(c, n, null).each {state << it.id}
       ]

    }

    def doUseNameNode() {
        classificationManagerService.fixClassificationUseNodeForName(Arrangement.findByLabel(params['classification']), Name.get(params['nameId']), Node.get(params['nodeId']));
        flash.message = "All placements of name ${params['nameId']} in ${params['classification']} merged into node  ${params['nodeId']}"
        redirect action: 'selectNameNode', params: [classification: params['classification'], nameId: params['nameId']]


    }

}
